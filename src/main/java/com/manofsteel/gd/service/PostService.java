package com.manofsteel.gd.service;

import com.manofsteel.gd.exception.NotFoundException;
import com.manofsteel.gd.exception.ThumbnailCreationException;
import com.manofsteel.gd.exception.ThumbnailIOException;
import com.manofsteel.gd.repository.*;
import com.manofsteel.gd.type.dto.PostDto;
import com.manofsteel.gd.type.entity.*;
import com.manofsteel.gd.type.vo.PostVo;
import com.manofsteel.gd.util.EntityUtil;
import com.manofsteel.gd.util.ImageUtil;
import jakarta.transaction.Transactional;
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import kr.co.shineware.nlp.komoran.model.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private static final String LOGIN_REQUIRED_MSG = "로그인 후 이용해주세요";
    private static final String POST_NOT_FOUND_MSG = "해당 글이 존재하지 않습니다.";
    private static final String WRONG_WRITER_MSG = "해당 글의 작성자가 아닙니다.";

    private void validatePostWriter(Post post, User writer) {
        if (!post.getWriterId().equals(writer)) {
            throw new IllegalArgumentException(WRONG_WRITER_MSG);
        }
    }

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final LikesRepository likesRepository;
    private final BookmarkRepository bookmarkRepository;
    private final TagRepository tagRepository;
    private final PostAndTagRelationRepository postAndTagRelationRepository;
    private final FollowRepository followRepository;
    private final UserService userService;
    private final ChatLogRepository chatLogRepository;
    private final ImageUtil imageUtil;

    //save
    @Transactional
    public Post save(PostDto postDto, Long writerId) throws ThumbnailIOException, ThumbnailCreationException {
        User user = userRepository.findByUserId(writerId)
                .orElseThrow(() -> new NotFoundException(LOGIN_REQUIRED_MSG));
        postDto.setWriterId(user);
        Post post = postRepository.save(postDto.toEntity());
        user.setPostCount(user.getPostCount() + 1);
        userRepository.save(user);
        return post;
    }

    @Transactional
    public Post save(Post post) throws ThumbnailIOException, ThumbnailCreationException {

        post.setThumbnailImgUrl(imageUtil.createThumbnail(post.getPostNum().toString()));

        return postRepository.save(post);
    }

    @Transactional
    public void delete(Long postNum, Long userId) {
        Post post = EntityUtil.findPost(postRepository, postNum);
        User user = EntityUtil.findUser(userRepository, userId);
        validatePostWriter(post, user);


        // Delete directory
        Path dir = Paths.get("cloud/post/" + postNum);
        if(Files.exists(dir)) {
            try {
                Files.walk(dir)
                        .sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
            } catch (IOException e) {
                throw new RuntimeException("Failed to delete directory!", e);
            }
        }

        postRepository.delete(post);
        user.setPostCount(user.getPostCount() - 1);
        userRepository.save(user);
    }
    //게시글과 댓글 출력
    @Transactional
    public PostDto getPost(Long postNum,Long userId){
        Post post = EntityUtil.findPost(postRepository, postNum);
        User writer = post.getWriterId();
        User user = EntityUtil.findUser(userRepository, userId);
        Boolean isMe = false;
        if(userId==writer.getUserId()){
            isMe = true;
        }
        String profileImgUrl = writer.getProfileImgUrl();
        Boolean isFollowing = followRepository.existsByFollowerIdAndFollowingId(userId, writer.getUserId());
        Boolean likes = likesRepository.existsLikesByUserIdAndPostNum(user, post);
        Boolean bookmark = bookmarkRepository.existsBookmarkByUserIdAndPostNum(user, post);
        List<PostAndTagRelation> postAndTag = postAndTagRelationRepository.findAllByPostNum(post);
        ChatLog chatLog = EntityUtil.findChatLog(chatLogRepository, post);
        List<Long> tagId = new ArrayList<>();
        for(PostAndTagRelation postAndTagRelation : postAndTag){
            tagId.add(postAndTagRelation.getTagId().getSerialNum());
        }

        PostDto postDto = PostDto.builder()
                .postNum(post.getPostNum())
                .content(post.getContent())
                .likesCount(post.getLikesCount())
                .timestamp(post.getTimestamp())
                .writerId(post.getWriterId())
                .thumbnailImgUrl(post.getThumbnailImgUrl())
                .bookmark(bookmark)
                .like(likes)
                .follow(isFollowing)
                .isMe(isMe)
                .description(chatLog.getMessage())
                .path(post.getPath())
                .profileImgUrl(profileImgUrl)
                .tagIds(tagId)
                .build();

        return postDto;
    }

    public List<Post> findAll(Long start, Long display) {
        List<Post> postList = postRepository.findAll(start, display);
        List<Post> results = postList.stream()
                .collect(Collectors.toList());
        return results;
    }

    @Transactional
    public boolean isPostBookmarkedByUser(Post post, User user) {
        return bookmarkRepository.existsBookmarkByUserIdAndPostNum(user, post);
    }
    @Transactional
    public boolean isMeByUser(Post post, User user) {
        return postRepository.existsPostByWriterIdAndPostNum(user, post.getPostNum());
    }


    @Transactional
    public List<PostVo> getPostsByWriterId(Long writerId) {
        User writer = userService.findUserById(writerId);
        List<Post> posts = postRepository.findByWriterId(writer);
        List<PostVo> postVos = new ArrayList<>();
        for (Post post : posts) {
            String thumbnailImgUrl = post.getThumbnailImgUrl();
            postVos.add(new PostVo(post.getPostNum(), thumbnailImgUrl));
        }
        return postVos;
    }

    @Transactional
    public List<PostVo> getLikedPostsByUserId(Long userId) {
        List<Likes> likedLikes = likesRepository.findLikesByUserId(new User(userId));
        List<PostVo> likedPostVos = new ArrayList<>();

        for (Likes likes : likedLikes) {
            Long postNum = likes.getPostNum().getPostNum();
            Optional<Post> postOptional = postRepository.findByPostNum(postNum);

            if (postOptional.isPresent()) {
                Post post = postOptional.get();
                String thumbnailImgUrl = post.getThumbnailImgUrl();
                likedPostVos.add(new PostVo(post.getPostNum(), thumbnailImgUrl));
            }
        }

        return likedPostVos;
    }

    @Transactional
    public List<PostVo> getBookmarkedPostsByUserId(Long userId) {
        List<Bookmark> bookmarks = bookmarkRepository.findBookmarkByUserId(new User(userId));
        List<PostVo> bookmarkedPostVos = new ArrayList<>();
        User user = EntityUtil.findUser(userRepository,userId);
        Boolean isMe;
        for (Bookmark bookmark : bookmarks) {
            Long postNum = bookmark.getPostNum().getPostNum();
            Optional<Post> postOptional = postRepository.findById(postNum);

            if (postOptional.isPresent()) {
                Post post = postOptional.get();
                isMe=isMeByUser(post, user);
                String thumbnailImgUrl = post.getThumbnailImgUrl();
                bookmarkedPostVos.add(new PostVo(post.getPostNum(), thumbnailImgUrl, true,isMe));
            }
        }
        return bookmarkedPostVos;
    }

    @Transactional
    public List<Post> getPopularPosts(Long start, Long display) {
        List<Post> postList = postRepository.findTop30ByOrderByLikesCountDesc();

        int startIndex = Math.min(start.intValue(), postList.size());
        int endIndex = Math.min(startIndex + display.intValue(), postList.size());

        postList = postList.subList(startIndex, endIndex);
        List<Post> top30 = new ArrayList<>();

        for (Post post : postList) {

            top30.add(post);
        }
        return top30;
    }

    @Transactional
    public List<Post> getFilteredPosts(String tagName, Long start, Long display) {

        Tag tags = tagRepository.findByTagName(tagName).orElseThrow(() -> new NotFoundException("해당 태그는 존재하지 않습니다."));

        List<PostAndTagRelation> postAndTagRelations = postAndTagRelationRepository.findAllByTagId(tags);
        List<Post> postList = new ArrayList<>();

        for (PostAndTagRelation postAndTag : postAndTagRelations) {
            Long postNum = postAndTag.getPostNum().getPostNum();
            Post post = EntityUtil.findPost(postRepository, postNum);
            if (post != null) {

                postList.add(post);
            }
        }
        int startIndex = Math.min(start.intValue(), postList.size());
        int endIndex = Math.min(startIndex + display.intValue(), postList.size());

        return postList.subList(startIndex, endIndex);
    }

    @Transactional
    public List<String> extractKeywords(String content){

        Komoran komoran = new Komoran(DEFAULT_MODEL.FULL);

        Map<String, Integer> keywordCounts = new HashMap<>();

        KomoranResult analyzeResult = komoran.analyze(content);

        for(Token token : analyzeResult.getTokenList()) {
            if(token.getPos().equals("NNG") || token.getPos().equals("NNP")) {
                keywordCounts.put(token.getMorph(), keywordCounts.getOrDefault(token.getMorph(), 0) + 1);
            }
        }
        Map.Entry<String, Integer>[] topKeywords = keywordCounts.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .limit(5)
                .toArray(Map.Entry[]::new);

        ArrayList<String> keywords = new ArrayList<>();
        for(int i = 0; i<1 || i<keywords.size(); i++) {
            keywords.add(topKeywords[i].getKey());
        }

        return keywords;
    }


    @Transactional
    public List<Post> searchPost(String keyword, Long start, Long display) {
        Set<Post> postSet = new HashSet<>();
        List<String> keywords = extractKeywords(keyword);

        for (String key : keywords) {
            List<Post> result = postRepository.findByContentContaining(key);
            postSet.addAll(result);
        }

        List<Post> postList = new ArrayList<>(postSet);
        int startIndex = Math.min(start.intValue(), postList.size());
        int endIndex = Math.min(startIndex + display.intValue(), postList.size());

        return postList.subList(startIndex, endIndex);
    }

}
package com.manofsteel.gd.repository;


import com.manofsteel.gd.type.entity.ChatLog;
import com.manofsteel.gd.type.entity.DalleImageItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface DalleImageRepository extends JpaRepository<DalleImageItem, Long> {


   Optional<DalleImageItem> findByItemId(Long itemId);

   List<DalleImageItem> findAllBySerialNum(ChatLog chatLog);

   DalleImageItem save(DalleImageItem dalleImageItem);


}

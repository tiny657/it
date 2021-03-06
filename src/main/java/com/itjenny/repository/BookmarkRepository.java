package com.itjenny.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itjenny.domain.Bookmark;
import com.itjenny.service.article.BookmarkPK;

public interface BookmarkRepository extends JpaRepository<Bookmark, BookmarkPK> {
}

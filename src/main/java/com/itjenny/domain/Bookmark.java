package com.itjenny.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;

import lombok.Data;

import com.itjenny.service.article.BookmarkPK;

@Data
@Entity
@IdClass(BookmarkPK.class)
public class Bookmark {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Id
	private String userId;
	@Id
	private String title;

	private Integer chapterIndex;
}
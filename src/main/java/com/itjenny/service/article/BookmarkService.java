package com.itjenny.service.article;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.itjenny.domain.Bookmark;
import com.itjenny.domain.user.SocialUser;
import com.itjenny.repository.BookmarkRepository;
import com.itjenny.support.Const;
import com.itjenny.support.security.SessionService;

@Service
public class BookmarkService {
	private final Logger logger = LoggerFactory.getLogger(BookmarkService.class);

	@Autowired
	private SessionService sessionService;

	@Autowired
	private BookmarkRepository bookmarkRepository;

	public Integer getChapterIndex(String title) {
		String providerUserId = sessionService.getLoginUser().getProviderUserId();
		if (providerUserId == null) {
			return 0;
		}
		Bookmark bookmark = bookmarkRepository.findOne(new BookmarkPK(providerUserId, title));
		if (bookmark == null) {
			return 0;
		}
		return bookmark.getChapterIndex();
	}
	
	public void updateChapter(String title, Integer chapterIndex) {
		String providerUserId = sessionService.getLoginUser().getProviderUserId();
		if (Strings.isNullOrEmpty(providerUserId)) {
			return;
		}
		Bookmark existedBookmark = bookmarkRepository.findOne(new BookmarkPK(providerUserId, title));
		if (existedBookmark == null) {
			Bookmark bookmark = new Bookmark();
			bookmark.setProviderUserId(providerUserId);
			bookmark.setTitle(title);
			bookmark.setChapterIndex(chapterIndex);
			bookmarkRepository.save(bookmark);
		} else if (existedBookmark.getChapterIndex() < chapterIndex) {
			Bookmark bookmark = new Bookmark();
			bookmark.setProviderUserId(providerUserId);
			bookmark.setTitle(title);
			bookmark.setChapterIndex(chapterIndex);
			bookmarkRepository.save(bookmark);
		}
	}

	public void complete(String title) {
		SocialUser loginUser = sessionService.getLoginUser();
		if (Strings.isNullOrEmpty(loginUser.getProviderUserId())) {
			return;
		}
		Bookmark bookmark = new Bookmark();
		bookmark.setProviderUserId(loginUser.getProviderUserId());
		bookmark.setTitle(title);
		bookmark.setChapterIndex(Const.BOOKMARK_LICENSE);
		bookmarkRepository.save(bookmark);
	}
}
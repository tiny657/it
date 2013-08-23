package com.itjenny.service.article;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itjenny.domain.Article;
import com.itjenny.domain.Chapter;
import com.itjenny.domain.HtmlArticle;
import com.itjenny.support.Const;

@Service
public class HtmlArticleService {
	private final Logger logger = LoggerFactory.getLogger(HtmlArticleService.class);

	@Autowired
	private ArticleService articleService;

	private Map<String, HtmlArticle> htmlArticles = new HashMap<String, HtmlArticle>();

	public HtmlArticle get(String title) {
		HtmlArticle htmlArticle = htmlArticles.get(title);
		if (htmlArticle == null) {
			Article article = articleService.get(title);
			if (article == null) {
				return null;
			}
			htmlArticles.put(title, new HtmlArticle(title, article.getContent()));
		}
		return htmlArticles.get(title);
	}

	public List<Chapter> getChaptersToIndex(String title, Integer toIndex) {
		HtmlArticle htmlArticle = get(title);
		if (htmlArticle == null) {
			return null;
		}
		if (toIndex.equals(Const.BOOKMARK_LICENSE)) {
			return htmlArticle.getChapters();
		}
		return htmlArticle.getChapters().subList(0, toIndex + 1);
	}

	public Chapter getChapter(String title, int index) {
		HtmlArticle htmlArticle = get(title);
		if (htmlArticle == null) {
			return null;
		}
		return htmlArticle.getChapters().get(index);
	}

	public boolean isChapterExisted(String title, int index) {
		HtmlArticle htmlArticle = get(title);
		if (htmlArticle == null) {
			return false;
		}
		if (htmlArticle.getChapters().size() <= index) {
			return false;
		}
		return true;
	}
}
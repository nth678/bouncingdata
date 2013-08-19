package com.bouncingdata.search.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.lucene.analysis.standard.StandardAnalyzer;

import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;

import com.bouncingdata.search.controller.HibernateEntitymanagerHelper;

import com.bouncingdata.search.model.ActivityIndexed;
import com.bouncingdata.search.model.AnalyseIndexed;
import com.bouncingdata.search.model.CommentIndexed;
import com.bouncingdata.search.model.DatasetIndexed;
import com.bouncingdata.search.model.ScraperIndexed;
import com.bouncingdata.search.model.SpringUserIndexed;
import com.bouncingdata.search.model.VisualizationIndexed;

public class HibernateSearcher {
	private EntityManager em;
	private FullTextEntityManager fullTextEntityManager;

	public HibernateSearcher() {
		em = HibernateEntitymanagerHelper.getEntityManagerFactory()
				.createEntityManager();
		fullTextEntityManager = Search.getFullTextEntityManager(em);

		try {
			fullTextEntityManager.createIndexer().startAndWait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void buildIndex() {
		indexing(ActivityIndexed.class);
		indexing(AnalyseIndexed.class);
		indexing(CommentIndexed.class);
		indexing(DatasetIndexed.class);
		indexing(ScraperIndexed.class);
		indexing(SpringUserIndexed.class);
		indexing(VisualizationIndexed.class);

	}

	public List<Object> doSearch(String keyword) {
		String[] fields = new String[] { "name", "description", "type" };
		search(keyword, fields, AnalyseIndexed.class);

		fields = new String[] { "message", "action" };
		search(keyword, fields, ActivityIndexed.class);

		fields = new String[] { "message", "title" };
		search(keyword, fields, CommentIndexed.class);

		fields = new String[] { "name", "description" };
		search(keyword, fields, DatasetIndexed.class);

		fields = new String[] { "userName", "password", "email",
				"firstName", "lastName" };
		search(keyword, fields, ScraperIndexed.class);
		
		fields = new String[] { "name", "description", "createSource",
				"language", "type" };		
		search(keyword, fields, SpringUserIndexed.class);
		
		fields = new String[] { "name", "description","type" };
		search(keyword, fields, VisualizationIndexed.class);

		return null;

	}

	public void terminate() {
		em.close();
		HibernateEntitymanagerHelper.shutdown();
	}

	private <T> void indexing(Class<T> c) {
		fullTextEntityManager.getTransaction().begin();
		@SuppressWarnings("unchecked")
		List<Object> list = em.createQuery(
				"select record from " + c.getName() + " as record")
				.getResultList();
		for (Object element : list) {
			fullTextEntityManager.index(element);
		}

		fullTextEntityManager.getTransaction().commit();

	}

	private <T> List<Object> search(String keyword, String[] fields, Class<T> c) {
		fullTextEntityManager.getTransaction().begin();

		MultiFieldQueryParser parser = new MultiFieldQueryParser(
				Version.LUCENE_35, fields, new StandardAnalyzer(
						Version.LUCENE_35));
		Query query = null;
		try {
			query = parser.parse(keyword);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// wrap Lucene query in a javax.persistence.Query
		javax.persistence.Query persistenceQuery = fullTextEntityManager
				.createFullTextQuery(query, c.getClasses());
		// execute search
		@SuppressWarnings("unchecked")
		List<Object> result = persistenceQuery.getResultList();

		System.out.println("Found :" + result.size());
		fullTextEntityManager.getTransaction().commit();

		return result;

	}

	/**
	 * @return the fullTextEntityManager
	 */
	public FullTextEntityManager getFullTextEntityManager() {
		return fullTextEntityManager;
	}

	/**
	 * @param fullTextEntityManager
	 *            the fullTextEntityManager to set
	 */
	public void setFullTextEntityManager(
			FullTextEntityManager fullTextEntityManager) {
		this.fullTextEntityManager = fullTextEntityManager;
	}
}

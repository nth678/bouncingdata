package com.bouncingdata.search.service;



public class TestHibernateSearch {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HibernateSearcher searcher = new HibernateSearcher();
		searcher.buildIndex();
		searcher.doSearch("123456");
		searcher.terminate();
	
		
	}

}

package com.bouncingdata.search.controller;


import javax.persistence.*;
public class HibernateEntitymanagerHelper {
	private static EntityManagerFactory emf;
	static{
		try{
			emf = Persistence.createEntityManagerFactory("defaultManager");			
		}catch(Throwable tw){
			throw new ExceptionInInitializerError(tw);
		}
	}
	public static EntityManagerFactory getEntityManagerFactory() {
		return emf;
	}
	public static void shutdown() {
		emf.close();
	}
}
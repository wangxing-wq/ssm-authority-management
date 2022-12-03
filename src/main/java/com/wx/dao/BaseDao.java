package com.wx.dao;

/**
 * @author 22343
 * @version 1.0
 */
public interface BaseDao<K,T> {
	
	/**
	 * 插入值
	 * @param bean 插入Bean对象值
	 * @return     是否插入成功
	 */
	boolean insert(T bean);
	
	/**
	 * 通过主键列进行删除
	 * @param key 主键
	 * @return    是否删除成功
	 */
	boolean delete(K key);
	
	/**
	 * 修改成功 Bean对象
	 * @param bean Bean对象值
	 * @return 是否修改成功
	 */
	boolean update(T bean);
	
	/**
	 * 通过Id 查找元素
	 * @param k 主键
	 * @return T  返回元素
	 */
	T findById(K k);
	
	
}

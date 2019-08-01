package com.ushareit.lucene.common;

import com.sun.rowset.CachedRowSetImpl;

import javax.sql.RowSet;
import javax.sql.rowset.CachedRowSet;
import java.sql.*;

public class DBManager {
	
	private static DBManager dbManager;
	
	protected DBManager() {
		
	}
	
	public static DBManager getInstance() {
		if (dbManager == null) {
			dbManager = new DBManager();
		}
		return dbManager;
	}

	/**
	 * 获得数据库连接
	 * <p>Title: getConnection</p>
	 * <p>Description: </p>
	 * @return
	 * @throws Exception
	 */
	private Connection getConnection() throws Exception {
		//加载数据库驱动
		Class.forName("com.mysql.jdbc.Driver");
		//创建数据库连接
		Connection connection = DriverManager.getConnection(Config.getConnectionUrl(), 
														Config.getConnectionUsername(), 
														Config.getConnectionPassword());
		//返回连接
		return connection;
		
	}
	
	/**
	 * 获得预处理对象
	 * <p>Title: getPreparedStatement</p>
	 * <p>Description: </p>
	 * @param connection
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	private PreparedStatement getPreparedStatement(Connection connection, String sql) throws SQLException {
		//创建预处理对象
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		return preparedStatement;
	}
	
	/**
	 * 查询数据库
	 * <p>Title: query</p>
	 * <p>Description: </p>
	 * @param sql
	 * @return
	 * @throws Exception 
	 */
	public RowSet query(String sql) throws Exception {
		//获得数据连接
		Connection connection = getConnection();
		//获得预处理
		PreparedStatement preparedStatement = getPreparedStatement(connection, sql);
		//查询数据库
		ResultSet resultSet = preparedStatement.executeQuery(sql);
		//创建数据缓存对象
		CachedRowSet rowSet = new CachedRowSetImpl();
		//填充数据集
		rowSet.populate(resultSet);
		//关闭数据集及数据库连接
		resultSet.close();
		preparedStatement.close();
		connection.close();
		
		//返回数据集
		return rowSet;
	}
	
	/**
	 * 执行数据库修改处理
	 * <p>Title: execute</p>
	 * <p>Description: </p>
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public boolean execute(String sql) throws Exception {
		//获得数据连接
		Connection connection = getConnection();
		//获得预处理
		PreparedStatement preparedStatement = getPreparedStatement(connection, sql);
		//查询数据库
		boolean result = false;
		try {
			result = preparedStatement.execute(sql);
		} catch (Exception e) {
			connection.rollback();
		}
		//提交数据库
		connection.commit();
		//关闭数据集及数据库连接
		preparedStatement.close();
		connection.close();
		
		return result;
	}
}

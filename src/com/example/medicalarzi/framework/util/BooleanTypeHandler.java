package com.example.medicalarzi.framework.util;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

@SuppressWarnings("rawtypes")
public class BooleanTypeHandler implements TypeHandler {
	static final String TRUE_STRING = "Y";
	static final String TRUE_INT = "1";
	static final String FALSE_STRING = "N";
	static final String FALSE_INT = "0";

	/**
	 * From Java to DB.
	 */
	public void setParameter(PreparedStatement ps, int i, Object parameter,
			JdbcType jdbcType) throws SQLException {
		if (parameter == null) {
			ps.setString(i, FALSE_STRING);
			return;
		}

		final Boolean bool = (Boolean) parameter;

		if (bool.booleanValue()) {
			ps.setString(i, TRUE_STRING);
		} else {
			ps.setString(i, FALSE_STRING);
		}
	}

	/**
	 * From DB to Java.
	 */
	public Object getResult(ResultSet rs, String columnName)
			throws SQLException {
		final String dbValue = trim(rs.getString(columnName));

		final Object result = valueOf(dbValue);

		return result;
	}

	public Object getResult(CallableStatement cs, int i) throws SQLException {
		final String dbValue = trim(cs.getString(i));

		final Object result = valueOf(dbValue);

		return result;
	}

	/**
	 * Converts DB value to the Java value.
	 */
	public Object valueOf(String s) {
		if (s == null) {
			return Boolean.FALSE;
		}

		final String value = trim(s);

		if (TRUE_STRING.equals(value) || TRUE_INT.equals(value)) {
			return Boolean.TRUE;
		}

		return Boolean.FALSE;
	}

	/**
	 * Trims the String if not null.
	 */
	static String trim(String string) {
		return (string == null) ? null : string.trim();
	}

	public Object getResult(ResultSet cs, int i) throws SQLException {
		final String dbValue = trim(cs.getString(i));

		final Object result = valueOf(dbValue);

		return result;
	}

}

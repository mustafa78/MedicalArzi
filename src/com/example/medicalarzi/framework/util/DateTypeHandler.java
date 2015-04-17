package com.example.medicalarzi.framework.util;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

public class DateTypeHandler implements TypeHandler<Date> {

   @Override
   public void setParameter(final PreparedStatement ps, final int i, Date date, final JdbcType jdbcType) throws SQLException {
      if (date == null) {
         ps.setNull(i, jdbcType.TYPE_CODE);
      }
      else if (date instanceof Timestamp) {
         ps.setTimestamp(i, new Timestamp(date.getTime()));
      }
      else {
         date = removeTime(date);
         Timestamp timestamp = new Timestamp(date.getTime());
         ps.setTimestamp(i, timestamp);
         //ps.setTimestamp(i, timestamp, getGMTCalendar());
      }
   }
   public static Date removeTime(Date date) {
      Calendar cal = Calendar.getInstance();
      cal.setTime(date);
      cal.set(Calendar.HOUR_OF_DAY, 0);
      cal.set(Calendar.MINUTE, 0);
      cal.set(Calendar.SECOND, 0);
      cal.set(Calendar.MILLISECOND, 0);
      return cal.getTime();
   }

   @Override
   public Date getResult(final ResultSet rs, final String columnName) throws SQLException {
      return rs.getTimestamp(columnName);
      //return rs.getTimestamp(columnName, getGMTCalendar());
   }

   @Override
   public Date getResult(final ResultSet rs, final int columnIndex) throws SQLException {
      return rs.getTimestamp(columnIndex);
      //return rs.getTimestamp(columnIndex, getGMTCalendar());
   }

   @Override
   public Date getResult(final CallableStatement cs, final int columnIndex) throws SQLException {
      return cs.getTimestamp(columnIndex);
      //return cs.getTimestamp(columnIndex, getGMTCalendar());
   }

   private static Calendar getGMTCalendar() {
      return Calendar.getInstance(TimeZone.getTimeZone("GMT"));
   }
}

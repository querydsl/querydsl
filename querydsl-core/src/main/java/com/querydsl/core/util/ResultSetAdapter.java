/*
 * Copyright 2011, Mysema Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.core.util;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

/**
 * @author tiwe
 *
 */
public class ResultSetAdapter implements ResultSet{

    private final ResultSet rs;

    public ResultSetAdapter(ResultSet rs) {
        this.rs = rs;
    }

    public boolean absolute(int row) throws SQLException {
        return rs.absolute(row);
    }

    public void afterLast() throws SQLException {
        rs.afterLast();
    }

    public void beforeFirst() throws SQLException {
        rs.beforeFirst();
    }

    public void cancelRowUpdates() throws SQLException {
        rs.cancelRowUpdates();
    }

    public void clearWarnings() throws SQLException {
        rs.clearWarnings();
    }

    public void close() throws SQLException {
        rs.close();
    }

    public void deleteRow() throws SQLException {
        rs.deleteRow();
    }

    public int findColumn(String columnLabel) throws SQLException {
        return rs.findColumn(columnLabel);
    }

    public boolean first() throws SQLException {
        return rs.first();
    }

    public Array getArray(int columnIndex) throws SQLException {
        return rs.getArray(columnIndex);
    }

    public Array getArray(String columnLabel) throws SQLException {
        return rs.getArray(columnLabel);
    }

    public InputStream getAsciiStream(int columnIndex) throws SQLException {
        return rs.getAsciiStream(columnIndex);
    }

    public InputStream getAsciiStream(String columnLabel) throws SQLException {
        return rs.getAsciiStream(columnLabel);
    }

    @SuppressWarnings("deprecation")
    public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException {
        return rs.getBigDecimal(columnIndex, scale);
    }

    public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
        return rs.getBigDecimal(columnIndex);
    }

    @SuppressWarnings("deprecation")
    public BigDecimal getBigDecimal(String columnLabel, int scale) throws SQLException {
        return rs.getBigDecimal(columnLabel, scale);
    }

    public BigDecimal getBigDecimal(String columnLabel) throws SQLException {
        return rs.getBigDecimal(columnLabel);
    }

    public InputStream getBinaryStream(int columnIndex) throws SQLException {
        return rs.getBinaryStream(columnIndex);
    }

    public InputStream getBinaryStream(String columnLabel) throws SQLException {
        return rs.getBinaryStream(columnLabel);
    }

    public Blob getBlob(int columnIndex) throws SQLException {
        return rs.getBlob(columnIndex);
    }

    public Blob getBlob(String columnLabel) throws SQLException {
        return rs.getBlob(columnLabel);
    }

    public boolean getBoolean(int columnIndex) throws SQLException {
        return rs.getBoolean(columnIndex);
    }

    public boolean getBoolean(String columnLabel) throws SQLException {
        return rs.getBoolean(columnLabel);
    }

    public byte getByte(int columnIndex) throws SQLException {
        return rs.getByte(columnIndex);
    }

    public byte getByte(String columnLabel) throws SQLException {
        return rs.getByte(columnLabel);
    }

    public byte[] getBytes(int columnIndex) throws SQLException {
        return rs.getBytes(columnIndex);
    }

    public byte[] getBytes(String columnLabel) throws SQLException {
        return rs.getBytes(columnLabel);
    }

    public Reader getCharacterStream(int columnIndex) throws SQLException {
        return rs.getCharacterStream(columnIndex);
    }

    public Reader getCharacterStream(String columnLabel) throws SQLException {
        return rs.getCharacterStream(columnLabel);
    }

    public Clob getClob(int columnIndex) throws SQLException {
        return rs.getClob(columnIndex);
    }

    public Clob getClob(String columnLabel) throws SQLException {
        return rs.getClob(columnLabel);
    }

    public int getConcurrency() throws SQLException {
        return rs.getConcurrency();
    }

    public String getCursorName() throws SQLException {
        return rs.getCursorName();
    }

    public Date getDate(int columnIndex, Calendar cal) throws SQLException {
        return rs.getDate(columnIndex, cal);
    }

    public Date getDate(int columnIndex) throws SQLException {
        return rs.getDate(columnIndex);
    }

    public Date getDate(String columnLabel, Calendar cal) throws SQLException {
        return rs.getDate(columnLabel, cal);
    }

    public Date getDate(String columnLabel) throws SQLException {
        return rs.getDate(columnLabel);
    }

    public double getDouble(int columnIndex) throws SQLException {
        return rs.getDouble(columnIndex);
    }

    public double getDouble(String columnLabel) throws SQLException {
        return rs.getDouble(columnLabel);
    }

    public int getFetchDirection() throws SQLException {
        return rs.getFetchDirection();
    }

    public int getFetchSize() throws SQLException {
        return rs.getFetchSize();
    }

    public float getFloat(int columnIndex) throws SQLException {
        return rs.getFloat(columnIndex);
    }

    public float getFloat(String columnLabel) throws SQLException {
        return rs.getFloat(columnLabel);
    }

    public int getHoldability() throws SQLException {
        return rs.getHoldability();
    }

    public int getInt(int columnIndex) throws SQLException {
        return rs.getInt(columnIndex);
    }

    public int getInt(String columnLabel) throws SQLException {
        return rs.getInt(columnLabel);
    }

    public long getLong(int columnIndex) throws SQLException {
        return rs.getLong(columnIndex);
    }

    public long getLong(String columnLabel) throws SQLException {
        return rs.getLong(columnLabel);
    }

    public ResultSetMetaData getMetaData() throws SQLException {
        return rs.getMetaData();
    }

    public Reader getNCharacterStream(int columnIndex) throws SQLException {
        return rs.getNCharacterStream(columnIndex);
    }

    public Reader getNCharacterStream(String columnLabel) throws SQLException {
        return rs.getNCharacterStream(columnLabel);
    }

    public NClob getNClob(int columnIndex) throws SQLException {
        return rs.getNClob(columnIndex);
    }

    public NClob getNClob(String columnLabel) throws SQLException {
        return rs.getNClob(columnLabel);
    }

    public String getNString(int columnIndex) throws SQLException {
        return rs.getNString(columnIndex);
    }

    public String getNString(String columnLabel) throws SQLException {
        return rs.getNString(columnLabel);
    }

    public Object getObject(int columnIndex, Map<String, Class<?>> map)
            throws SQLException {
        return rs.getObject(columnIndex, map);
    }

    public Object getObject(int columnIndex) throws SQLException {
        return rs.getObject(columnIndex);
    }
    
    public <T> T getObject(int columnIndex, Class<T> type) throws SQLException {
        // this getObject(int, Class) is available in JDK 7
        return (T)rs.getObject(columnIndex);
    }

    public Object getObject(String columnLabel, Map<String, Class<?>> map)
            throws SQLException {
        return rs.getObject(columnLabel, map);
    }

    public Object getObject(String columnLabel) throws SQLException {
        return rs.getObject(columnLabel);
    }
    
    public <T> T getObject(String columnLabel, Class<T> type) throws SQLException {
        // this getObject(String, Class) is available in JDK 7
        return (T)rs.getObject(columnLabel);
    }

    public Ref getRef(int columnIndex) throws SQLException {
        return rs.getRef(columnIndex);
    }

    public Ref getRef(String columnLabel) throws SQLException {
        return rs.getRef(columnLabel);
    }

    public int getRow() throws SQLException {
        return rs.getRow();
    }

    public RowId getRowId(int columnIndex) throws SQLException {
        return rs.getRowId(columnIndex);
    }

    public RowId getRowId(String columnLabel) throws SQLException {
        return rs.getRowId(columnLabel);
    }

    public short getShort(int columnIndex) throws SQLException {
        return rs.getShort(columnIndex);
    }

    public short getShort(String columnLabel) throws SQLException {
        return rs.getShort(columnLabel);
    }

    public SQLXML getSQLXML(int columnIndex) throws SQLException {
        return rs.getSQLXML(columnIndex);
    }

    public SQLXML getSQLXML(String columnLabel) throws SQLException {
        return rs.getSQLXML(columnLabel);
    }

    public Statement getStatement() throws SQLException {
        return rs.getStatement();
    }

    public String getString(int columnIndex) throws SQLException {
        return rs.getString(columnIndex);
    }

    public String getString(String columnLabel) throws SQLException {
        return rs.getString(columnLabel);
    }

    public Time getTime(int columnIndex, Calendar cal) throws SQLException {
        return rs.getTime(columnIndex, cal);
    }

    public Time getTime(int columnIndex) throws SQLException {
        return rs.getTime(columnIndex);
    }

    public Time getTime(String columnLabel, Calendar cal) throws SQLException {
        return rs.getTime(columnLabel, cal);
    }

    public Time getTime(String columnLabel) throws SQLException {
        return rs.getTime(columnLabel);
    }

    public Timestamp getTimestamp(int columnIndex, Calendar cal)
            throws SQLException {
        return rs.getTimestamp(columnIndex, cal);
    }

    public Timestamp getTimestamp(int columnIndex) throws SQLException {
        return rs.getTimestamp(columnIndex);
    }

    public Timestamp getTimestamp(String columnLabel, Calendar cal)
            throws SQLException {
        return rs.getTimestamp(columnLabel, cal);
    }

    public Timestamp getTimestamp(String columnLabel) throws SQLException {
        return rs.getTimestamp(columnLabel);
    }

    public int getType() throws SQLException {
        return rs.getType();
    }

    @SuppressWarnings("deprecation")
    public InputStream getUnicodeStream(int columnIndex) throws SQLException {
        return rs.getUnicodeStream(columnIndex);
    }

    @SuppressWarnings("deprecation")
    public InputStream getUnicodeStream(String columnLabel) throws SQLException {
        return rs.getUnicodeStream(columnLabel);
    }

    public URL getURL(int columnIndex) throws SQLException {
        return rs.getURL(columnIndex);
    }

    public URL getURL(String columnLabel) throws SQLException {
        return rs.getURL(columnLabel);
    }

    public SQLWarning getWarnings() throws SQLException {
        return rs.getWarnings();
    }

    public void insertRow() throws SQLException {
        rs.insertRow();
    }

    public boolean isAfterLast() throws SQLException {
        return rs.isAfterLast();
    }

    public boolean isBeforeFirst() throws SQLException {
        return rs.isBeforeFirst();
    }

    public boolean isClosed() throws SQLException {
        return rs.isClosed();
    }

    public boolean isFirst() throws SQLException {
        return rs.isFirst();
    }

    public boolean isLast() throws SQLException {
        return rs.isLast();
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return rs.isWrapperFor(iface);
    }

    public boolean last() throws SQLException {
        return rs.last();
    }

    public void moveToCurrentRow() throws SQLException {
        rs.moveToCurrentRow();
    }

    public void moveToInsertRow() throws SQLException {
        rs.moveToInsertRow();
    }

    public boolean next() throws SQLException {
        return rs.next();
    }

    public boolean previous() throws SQLException {
        return rs.previous();
    }

    public void refreshRow() throws SQLException {
        rs.refreshRow();
    }

    public boolean relative(int rows) throws SQLException {
        return rs.relative(rows);
    }

    public boolean rowDeleted() throws SQLException {
        return rs.rowDeleted();
    }

    public boolean rowInserted() throws SQLException {
        return rs.rowInserted();
    }

    public boolean rowUpdated() throws SQLException {
        return rs.rowUpdated();
    }

    public void setFetchDirection(int direction) throws SQLException {
        rs.setFetchDirection(direction);
    }

    public void setFetchSize(int rows) throws SQLException {
        rs.setFetchSize(rows);
    }

    public <T> T unwrap(Class<T> iface) throws SQLException {
        return rs.unwrap(iface);
    }

    public void updateArray(int columnIndex, Array x) throws SQLException {
        rs.updateArray(columnIndex, x);
    }

    public void updateArray(String columnLabel, Array x) throws SQLException {
        rs.updateArray(columnLabel, x);
    }

    public void updateAsciiStream(int columnIndex, InputStream x, int length)
            throws SQLException {
        rs.updateAsciiStream(columnIndex, x, length);
    }

    public void updateAsciiStream(int columnIndex, InputStream x, long length)
            throws SQLException {
        rs.updateAsciiStream(columnIndex, x, length);
    }

    public void updateAsciiStream(int columnIndex, InputStream x)
            throws SQLException {
        rs.updateAsciiStream(columnIndex, x);
    }

    public void updateAsciiStream(String columnLabel, InputStream x, int length)
            throws SQLException {
        rs.updateAsciiStream(columnLabel, x, length);
    }

    public void updateAsciiStream(String columnLabel, InputStream x, long length)
            throws SQLException {
        rs.updateAsciiStream(columnLabel, x, length);
    }

    public void updateAsciiStream(String columnLabel, InputStream x)
            throws SQLException {
        rs.updateAsciiStream(columnLabel, x);
    }

    public void updateBigDecimal(int columnIndex, BigDecimal x)
            throws SQLException {
        rs.updateBigDecimal(columnIndex, x);
    }

    public void updateBigDecimal(String columnLabel, BigDecimal x)
            throws SQLException {
        rs.updateBigDecimal(columnLabel, x);
    }

    public void updateBinaryStream(int columnIndex, InputStream x, int length)
            throws SQLException {
        rs.updateBinaryStream(columnIndex, x, length);
    }

    public void updateBinaryStream(int columnIndex, InputStream x, long length)
            throws SQLException {
        rs.updateBinaryStream(columnIndex, x, length);
    }

    public void updateBinaryStream(int columnIndex, InputStream x)
            throws SQLException {
        rs.updateBinaryStream(columnIndex, x);
    }

    public void updateBinaryStream(String columnLabel, InputStream x, int length)
            throws SQLException {
        rs.updateBinaryStream(columnLabel, x, length);
    }

    public void updateBinaryStream(String columnLabel, InputStream x,
            long length) throws SQLException {
        rs.updateBinaryStream(columnLabel, x, length);
    }

    public void updateBinaryStream(String columnLabel, InputStream x)
            throws SQLException {
        rs.updateBinaryStream(columnLabel, x);
    }

    public void updateBlob(int columnIndex, Blob x) throws SQLException {
        rs.updateBlob(columnIndex, x);
    }

    public void updateBlob(int columnIndex, InputStream inputStream, long length)
            throws SQLException {
        rs.updateBlob(columnIndex, inputStream, length);
    }

    public void updateBlob(int columnIndex, InputStream inputStream)
            throws SQLException {
        rs.updateBlob(columnIndex, inputStream);
    }

    public void updateBlob(String columnLabel, Blob x) throws SQLException {
        rs.updateBlob(columnLabel, x);
    }

    public void updateBlob(String columnLabel, InputStream inputStream,
            long length) throws SQLException {
        rs.updateBlob(columnLabel, inputStream, length);
    }

    public void updateBlob(String columnLabel, InputStream inputStream)
            throws SQLException {
        rs.updateBlob(columnLabel, inputStream);
    }

    public void updateBoolean(int columnIndex, boolean x) throws SQLException {
        rs.updateBoolean(columnIndex, x);
    }

    public void updateBoolean(String columnLabel, boolean x)
            throws SQLException {
        rs.updateBoolean(columnLabel, x);
    }

    public void updateByte(int columnIndex, byte x) throws SQLException {
        rs.updateByte(columnIndex, x);
    }

    public void updateByte(String columnLabel, byte x) throws SQLException {
        rs.updateByte(columnLabel, x);
    }

    public void updateBytes(int columnIndex, byte[] x) throws SQLException {
        rs.updateBytes(columnIndex, x);
    }

    public void updateBytes(String columnLabel, byte[] x) throws SQLException {
        rs.updateBytes(columnLabel, x);
    }

    public void updateCharacterStream(int columnIndex, Reader x, int length)
            throws SQLException {
        rs.updateCharacterStream(columnIndex, x, length);
    }

    public void updateCharacterStream(int columnIndex, Reader x, long length)
            throws SQLException {
        rs.updateCharacterStream(columnIndex, x, length);
    }

    public void updateCharacterStream(int columnIndex, Reader x)
            throws SQLException {
        rs.updateCharacterStream(columnIndex, x);
    }

    public void updateCharacterStream(String columnLabel, Reader reader,
            int length) throws SQLException {
        rs.updateCharacterStream(columnLabel, reader, length);
    }

    public void updateCharacterStream(String columnLabel, Reader reader,
            long length) throws SQLException {
        rs.updateCharacterStream(columnLabel, reader, length);
    }

    public void updateCharacterStream(String columnLabel, Reader reader)
            throws SQLException {
        rs.updateCharacterStream(columnLabel, reader);
    }

    public void updateClob(int columnIndex, Clob x) throws SQLException {
        rs.updateClob(columnIndex, x);
    }

    public void updateClob(int columnIndex, Reader reader, long length)
            throws SQLException {
        rs.updateClob(columnIndex, reader, length);
    }

    public void updateClob(int columnIndex, Reader reader) throws SQLException {
        rs.updateClob(columnIndex, reader);
    }

    public void updateClob(String columnLabel, Clob x) throws SQLException {
        rs.updateClob(columnLabel, x);
    }

    public void updateClob(String columnLabel, Reader reader, long length)
            throws SQLException {
        rs.updateClob(columnLabel, reader, length);
    }

    public void updateClob(String columnLabel, Reader reader)
            throws SQLException {
        rs.updateClob(columnLabel, reader);
    }

    public void updateDate(int columnIndex, Date x) throws SQLException {
        rs.updateDate(columnIndex, x);
    }

    public void updateDate(String columnLabel, Date x) throws SQLException {
        rs.updateDate(columnLabel, x);
    }

    public void updateDouble(int columnIndex, double x) throws SQLException {
        rs.updateDouble(columnIndex, x);
    }

    public void updateDouble(String columnLabel, double x) throws SQLException {
        rs.updateDouble(columnLabel, x);
    }

    public void updateFloat(int columnIndex, float x) throws SQLException {
        rs.updateFloat(columnIndex, x);
    }

    public void updateFloat(String columnLabel, float x) throws SQLException {
        rs.updateFloat(columnLabel, x);
    }

    public void updateInt(int columnIndex, int x) throws SQLException {
        rs.updateInt(columnIndex, x);
    }

    public void updateInt(String columnLabel, int x) throws SQLException {
        rs.updateInt(columnLabel, x);
    }

    public void updateLong(int columnIndex, long x) throws SQLException {
        rs.updateLong(columnIndex, x);
    }

    public void updateLong(String columnLabel, long x) throws SQLException {
        rs.updateLong(columnLabel, x);
    }

    public void updateNCharacterStream(int columnIndex, Reader x, long length)
            throws SQLException {
        rs.updateNCharacterStream(columnIndex, x, length);
    }

    public void updateNCharacterStream(int columnIndex, Reader x)
            throws SQLException {
        rs.updateNCharacterStream(columnIndex, x);
    }

    public void updateNCharacterStream(String columnLabel, Reader reader,
            long length) throws SQLException {
        rs.updateNCharacterStream(columnLabel, reader, length);
    }

    public void updateNCharacterStream(String columnLabel, Reader reader)
            throws SQLException {
        rs.updateNCharacterStream(columnLabel, reader);
    }

    public void updateNClob(int columnIndex, NClob nClob) throws SQLException {
        rs.updateNClob(columnIndex, nClob);
    }

    public void updateNClob(int columnIndex, Reader reader, long length)
            throws SQLException {
        rs.updateNClob(columnIndex, reader, length);
    }

    public void updateNClob(int columnIndex, Reader reader) throws SQLException {
        rs.updateNClob(columnIndex, reader);
    }

    public void updateNClob(String columnLabel, NClob nClob)
            throws SQLException {
        rs.updateNClob(columnLabel, nClob);
    }

    public void updateNClob(String columnLabel, Reader reader, long length)
            throws SQLException {
        rs.updateNClob(columnLabel, reader, length);
    }

    public void updateNClob(String columnLabel, Reader reader)
            throws SQLException {
        rs.updateNClob(columnLabel, reader);
    }

    public void updateNString(int columnIndex, String nString)
            throws SQLException {
        rs.updateNString(columnIndex, nString);
    }

    public void updateNString(String columnLabel, String nString)
            throws SQLException {
        rs.updateNString(columnLabel, nString);
    }

    public void updateNull(int columnIndex) throws SQLException {
        rs.updateNull(columnIndex);
    }

    public void updateNull(String columnLabel) throws SQLException {
        rs.updateNull(columnLabel);
    }

    public void updateObject(int columnIndex, Object x, int scaleOrLength)
            throws SQLException {
        rs.updateObject(columnIndex, x, scaleOrLength);
    }

    public void updateObject(int columnIndex, Object x) throws SQLException {
        rs.updateObject(columnIndex, x);
    }

    public void updateObject(String columnLabel, Object x, int scaleOrLength)
            throws SQLException {
        rs.updateObject(columnLabel, x, scaleOrLength);
    }

    public void updateObject(String columnLabel, Object x) throws SQLException {
        rs.updateObject(columnLabel, x);
    }

    public void updateRef(int columnIndex, Ref x) throws SQLException {
        rs.updateRef(columnIndex, x);
    }

    public void updateRef(String columnLabel, Ref x) throws SQLException {
        rs.updateRef(columnLabel, x);
    }

    public void updateRow() throws SQLException {
        rs.updateRow();
    }

    public void updateRowId(int columnIndex, RowId x) throws SQLException {
        rs.updateRowId(columnIndex, x);
    }

    public void updateRowId(String columnLabel, RowId x) throws SQLException {
        rs.updateRowId(columnLabel, x);
    }

    public void updateShort(int columnIndex, short x) throws SQLException {
        rs.updateShort(columnIndex, x);
    }

    public void updateShort(String columnLabel, short x) throws SQLException {
        rs.updateShort(columnLabel, x);
    }

    public void updateSQLXML(int columnIndex, SQLXML xmlObject)
            throws SQLException {
        rs.updateSQLXML(columnIndex, xmlObject);
    }

    public void updateSQLXML(String columnLabel, SQLXML xmlObject)
            throws SQLException {
        rs.updateSQLXML(columnLabel, xmlObject);
    }

    public void updateString(int columnIndex, String x) throws SQLException {
        rs.updateString(columnIndex, x);
    }

    public void updateString(String columnLabel, String x) throws SQLException {
        rs.updateString(columnLabel, x);
    }

    public void updateTime(int columnIndex, Time x) throws SQLException {
        rs.updateTime(columnIndex, x);
    }

    public void updateTime(String columnLabel, Time x) throws SQLException {
        rs.updateTime(columnLabel, x);
    }

    public void updateTimestamp(int columnIndex, Timestamp x)
            throws SQLException {
        rs.updateTimestamp(columnIndex, x);
    }

    public void updateTimestamp(String columnLabel, Timestamp x)
            throws SQLException {
        rs.updateTimestamp(columnLabel, x);
    }

    public boolean wasNull() throws SQLException {
        return rs.wasNull();
    }

}

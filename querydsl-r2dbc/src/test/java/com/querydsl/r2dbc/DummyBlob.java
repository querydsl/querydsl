/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
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
package com.querydsl.r2dbc;

import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;

public class DummyBlob implements Blob {

    @Override
    public void free() {
        // TODO Auto-generated method stub

    }

    @Override
    public InputStream getBinaryStream() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public InputStream getBinaryStream(long pos, long length) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public byte[] getBytes(long pos, int length) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long length() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long position(byte[] pattern, long start) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long position(Blob pattern, long start) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public OutputStream setBinaryStream(long pos) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int setBytes(long pos, byte[] bytes) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int setBytes(long pos, byte[] bytes, int offset, int len) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void truncate(long len) {
        // TODO Auto-generated method stub

    }

}

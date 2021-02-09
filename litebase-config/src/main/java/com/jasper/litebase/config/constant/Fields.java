/*
 * Copyright 1999-2012 Alibaba Group.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jasper.litebase.config.constant;

/**
 * 字段类型及标识定义
 * 
 * @author xianmao.hexm
 */
public class Fields {
    /** field flag */
    public static final int NOT_NULL_FLAG = 0x0001;
    public static final int PRI_KEY_FLAG = 0x0002;
    public static final int UNIQUE_KEY_FLAG = 0x0004;
    public static final int MULTIPLE_KEY_FLAG = 0x0008;
    public static final int BLOB_FLAG = 0x0010;
    public static final int UNSIGNED_FLAG = 0x0020;
    public static final int ZEROFILL_FLAG = 0x0040;
    public static final int BINARY_FLAG = 0x0080;
    public static final int ENUM_FLAG = 0x0100;
    public static final int AUTO_INCREMENT_FLAG = 0x0200;
    public static final int TIMESTAMP_FLAG = 0x0400;
    public static final int SET_FLAG = 0x0800;
}

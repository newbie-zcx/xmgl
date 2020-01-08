package com.scsoft.wlyz.generator.modle;

/**
 * @title: DbTypeDriver
 * @Description:
 * @Author: zhaopengfei
 * @copyright: 雪城软件有限公司
 * All rights Reserved，Designed By Scsoft
 * @CreateDate: 2019/8/7 9:31
 * @Version: 1.0
 */
public enum DbTypeDriver {
    MYSQL("mysql", "com.mysql.jdbc.Driver", "MySql数据库"),
    MARIADB("mariadb", "org.mariadb.jdbc.Driver", "MariaDB数据库"),
    ORACLE("oracle", "oracle.jdbc.driver.OracleDriver", "Oracle数据库"),
    DB2("db2", "com.ibm.db2.jcc.DB2Driver", "DB2数据库"),
    H2("h2", "org.h2.Driver", "H2数据库"),
    HSQL("hsql", "org.hsqldb.jdbcDriver", "HSQL数据库"),
    SQLITE("sqlite", "SQLite3 ODBC Driver", "SQLite数据库"),
    POSTGRE_SQL("postgresql", "org.postgresql.Driver", "Postgre数据库"),
    SQL_SERVER2005("sqlserver2005", "com.microsoft.sqlserver.jdbc.SQLServerDriver", "SQLServer2005数据库"),
    SQL_SERVER("sqlserver", "com.microsoft.jdbc.sqlserver.SQLServer", "SQLServer数据库"),
    DM("dm", "dm.jdbc.driver.DmDriver", "达梦数据库"),
    OTHER("other", "", "其他数据库");
    private final String db;
    private final String driver;
    private final String desc;

    private DbTypeDriver(String db, String driver, String desc) {
        this.db = db;
        this.driver = driver;
        this.desc = desc;
    }

    public static DbTypeDriver getDbType(String dbType) {
        DbTypeDriver[] dts = values();
        DbTypeDriver[] var2 = dts;
        int var3 = dts.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            DbTypeDriver dt = var2[var4];
            if (dt.getDb().equalsIgnoreCase(dbType)) {
                return dt;
            }
        }

        return OTHER;
    }
    public static String getDbByDriver(String driver) {
        DbTypeDriver[] dts = values();
        DbTypeDriver[] var2 = dts;
        int var3 = dts.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            DbTypeDriver dt = var2[var4];
            if (dt.getDriver().equalsIgnoreCase(driver)) {
                return dt.db;
            }
        }

        return "other";
    }
    public String getDb() {
        return this.db;
    }


    public String getDriver() {
        return driver;
    }

    public String getDesc() {
        return this.desc;
    }
}

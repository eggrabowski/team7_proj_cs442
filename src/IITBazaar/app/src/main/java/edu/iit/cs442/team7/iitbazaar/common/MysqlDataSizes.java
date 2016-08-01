package edu.iit.cs442.team7.iitbazaar.common;



/**
 * @author <a href="mailto:jnosek@hawk.iit.edu">Janusz M. Nosek</a>
 */


public enum MysqlDataSizes {

    TINYINT(1),
    SMALLINT(2),
    MEDIUMINT(3),
    INT(4),
    INTEGER(4),
    BIGINT(8),
    FLOAT(4),
    DOUBLE(8),
    TINYBLOB(255),
    TINYTEXT(255),
    BLOB(65536),
    TEXT(65536),
    MEDIUMBLOB(16777216),
    MEDIUMTEXT(16777216),
    LONGBLOB(4294967296L),
    LONGTEXT(4294967296L),
    YEAR(1),
    DATE(3),
    TIME(3),
    DATETIME(8),
    TIMESTAMP(4),
    BINARY(255),
    CHAR(65535),
    VARCHAR(65535),
    VARBINARY(65535),
    //Wrong
    DECIMAL(0),
    //Wrong
    NUMERIC(0),
    //Unknown
    ENUM(65535),
    SET(512),
    BIT(8),
    ROW(65535);

    private final long bytes;

    MysqlDataSizes(long bytes) {
        this.bytes = bytes;

    }


    public long getBytes() { return bytes; }





}

package com.github.kaspiandev.fishybusiness.data;

public abstract class Table {

    protected final Database database;
    protected final String tableStatement;

    protected Table(Database database, String tableStatement) {
        this.database = database;
        this.tableStatement = tableStatement;
    }

    public String getTableStatement() {
        return tableStatement;
    }

}

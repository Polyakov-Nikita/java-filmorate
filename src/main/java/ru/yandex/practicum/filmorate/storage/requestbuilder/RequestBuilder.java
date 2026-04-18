package ru.yandex.practicum.filmorate.storage.requestbuilder;

import org.springframework.stereotype.Component;

@Component
public abstract class RequestBuilder {
    protected DBRequest createRequest(String query, Object... params) {
        return new DBRequest(query, params);
    }

    protected DBRequest getById(String tableName, String idName, long id) {
        String query = buildSelectQuery(tableName, idName);
        return createRequest(query, id);
    }

    public String buildInsertQuery(String tableName, String[] columns) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("INSERT INTO %s (", tableName));
        appendFieldNames(stringBuilder, columns);
        stringBuilder.append(") VALUES (");
        appendFieldValues(stringBuilder, columns.length);
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    private void appendFieldNames(StringBuilder stringBuilder, String[] columns) {
        for (int i = 0; i < columns.length; i++) {
            stringBuilder.append(columns[i]);
            if (i < columns.length - 1) {
                stringBuilder.append(", ");
            }
        }
    }

    private void appendFieldValues(StringBuilder stringBuilder, int fieldsCount) {
        for (int i = 0; i < fieldsCount; i++) {
            stringBuilder.append("?");
            if (i < fieldsCount - 1) {
                stringBuilder.append(", ");
            }
        }
    }

    public String buildUpdateQuery(String tableName, String[] columns) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("UPDATE %s SET ", tableName));
        appendFieldsUpdate(stringBuilder, columns);
        return stringBuilder.toString();
    }

    private void appendFieldsUpdate(StringBuilder stringBuilder, String[] columns) {
        for (int i = 0; i < columns.length; i++) {
            stringBuilder.append(String.format("%s = ?", columns[i]));
            if (i < columns.length - 1) {
                stringBuilder.append(", ");
            }
        }
    }

    public String buildSelectQuery(String tableName, String parameter) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("SELECT * FROM %s", tableName));
        if (parameter != null) {
            stringBuilder.append(String.format(" WHERE %s = ?", parameter));
        }
        return stringBuilder.toString();
    }

    public String buildSelectQuery(String tableName) {
        return buildSelectQuery(tableName, null);
    }

    public String buildDeleteQuery(String tableName, String... params) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("DELETE FROM %s WHERE ", tableName));
        appendParameters(stringBuilder, params);
        return stringBuilder.toString();
    }

    private void appendParameters(StringBuilder stringBuilder, String[] params) {
        for (int i = 0; i < params.length; i++) {
            stringBuilder.append(String.format("%s = ?", params[i]));
            if (i < params.length - 1) {
                stringBuilder.append(" AND ");
            }
        }
    }
}

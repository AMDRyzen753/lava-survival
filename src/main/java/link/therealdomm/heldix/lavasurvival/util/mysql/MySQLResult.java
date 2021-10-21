package link.therealdomm.heldix.lavasurvival.util.mysql;

import com.google.gson.internal.Primitives;
import link.therealdomm.heldix.lavasurvival.LavaSurvivalPlugin;
import lombok.Getter;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;

/**
 * a simple class that stores the mysql result
 *
 * @author TheRealDomm
 * @since 10.10.2021
 */
public class MySQLResult {

    @Getter private final List<Result> results = new ArrayList<>();

    /**
     * builds the {@link MySQLResult} by using the mysql {@link ResultSet}
     * @param resultSet to build result from
     * @return the null if the result could not be built
     */
    public static MySQLResult build(ResultSet resultSet) {
        if (resultSet == null) {
            throw new IllegalArgumentException("Could not build result because set was null!");
        }
        try {
            MySQLResult mySQLResult = new MySQLResult();
            ResultSetMetaData metaData = resultSet.getMetaData();
            while (resultSet.next()) {
                Result result = new Result();
                for (int i = 0; i < metaData.getColumnCount(); i++) {
                    String name = metaData.getColumnName(i+1);
                    result.addData(name, resultSet.getObject(name));
                }
                mySQLResult.getResults().add(result);
            }
            resultSet.close();
            return mySQLResult;
        } catch (SQLException e) {
            LavaSurvivalPlugin.getInstance().getLogger().log(Level.SEVERE, "", e);
        }
        return null;
    }

    /**
     * the data holding Result class for one row of the database
     */
    public static class Result {
        public static final Integer DEFAULT_INTEGER = -1;
        public static final String DEFAULT_STRING = "";

        @Getter private final Map<String, Object> dataSet = new LinkedHashMap<>();

        /**
         * add data to the dataSet
         * @param name of the column
         * @param data of the column
         */
        public void addData(String name, Object data) {
            this.dataSet.put(name, data);
        }

        /**
         * checks if the data of the specified column exists
         * @param name of the column
         * @return true if data exists
         */
        public boolean hasData(String name) {
            return this.dataSet.containsKey(name);
        }

        /**
         * get the data specifying the class that should be cast
         * @param name of the column
         * @param type to be cast
         * @param <T> type of return
         * @return the data if exists else null
         */
        public <T> T getData(String name, Class<? extends T> type) {
            Object o = this.dataSet.get(name);
            if (o != null) {
                return Primitives.wrap(type).cast(o);
            }
            return null;
        }

        /**
         * get an integer value from the dataSet
         * @param name of the column
         * @return value from dataSet else DEFAULT_INTEGER
         */
        public Integer getInteger(String name) {
            Object o = this.dataSet.get(name);
            if (o instanceof Integer) {
                return (Integer) o;
            }
            return DEFAULT_INTEGER;
        }

        /**
         * get a string value from the dataSet
         * @param name of the column
         * @return value from dataSet else DEFAULT_STRING
         */
        public String getString(String name) {
            Object o = this.dataSet.get(name);
            if (o instanceof String) {
                return (String) o;
            }
            return DEFAULT_STRING;
        }

        /**
         * get a uuid from the dataSet
         * @param name of the column
         * @return value from dataSet else null
         */
        public UUID getUuid(String name) {
            Object o = this.dataSet.get(name);
            try {
                if (o instanceof String) {
                    return UUID.fromString((String) o);
                }
            } catch (IllegalArgumentException e) {
                LavaSurvivalPlugin.getInstance().getLogger().log(Level.WARNING, "", e);
            }
            return null;
        }
    }

}

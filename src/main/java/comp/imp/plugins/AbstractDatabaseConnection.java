package comp.imp.plugins;

import comp.IPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public abstract class AbstractDatabaseConnection {

    /**
     * Connection settings: URL, User, Password!
     */
    private String _url, _user, _pwd;

    protected Connection _connection;

    AbstractDatabaseConnection(String url, String name, String password){
        _url = url;
        _user = name;
        _pwd = password;
    }

    /**
     * Connect to a sample database
     */
    protected void _createAndOrConnectToDatabase()
    {
        this._connection = null;
        if(_user.equals("")||_pwd.equals("")){
            try {
                _connection = DriverManager.getConnection(_url);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } else {
            try {
                _connection = DriverManager.getConnection(_url, _user, _pwd);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        if (_connection != null) {
            try {
                _connection.setAutoCommit(false);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Closing Connection!
     */
    protected void _close(){
        Connection conn = _connection;
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Prints all tables of a connection!
     */
    protected void _listOfTables(){
        String sql = "SELECT name FROM sqlite_master WHERE type ='table' AND name NOT LIKE 'sqlite_%';";
        _for(sql, null, (rs)->{
            try {
                System.out.println("\n"+rs.getString("name") + "");
                _selectAllFrom(rs.getString("name"));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Prints all tables of a connection!
     */
    protected String[] _listOfAllTables(){
        String sql = "SELECT name FROM sqlite_master WHERE type ='table' AND name NOT LIKE 'sqlite_%';";
        List<String> names = new ArrayList<>();
        _for(sql, null, rs -> {
            try {
                names.add(rs.getString("name"));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
        return names.toArray(new String[0]);
    }

    protected Map<String, String[]> _tablesSpace(){
        String sql = "SELECT * FROM sqlite_master WHERE type ='table' AND name NOT LIKE 'sqlite_%';";
        Map<String, String[]> space = new HashMap<>();
        _for(sql, null, rs -> {
            try {
                String def = rs.getString("sql");
                def = def.split("\\(")[1];
                def = def.split("\\)")[0];
                String[] payload = def.split(",");
                for(int i=0; i<payload.length; i++) payload[i] = payload[i].trim();
                space.put(rs.getString("name"), payload);

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
        return space;
    }

    protected void _for(String sql, Consumer<ResultSet> start, Consumer<ResultSet> each){
        try {
            Statement stmt = _connection.createStatement();
            try {
                ResultSet rs = stmt.executeQuery(sql);// loop through the result set
                if(start!=null) start.accept(rs);
                if(each!=null) do each.accept(rs); while(rs.next());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    /**
     * Defines and executes a SELECT * FROM on a connection.
     * @param tableName
     */
    private void _selectAllFrom(String tableName){
        String sql = "SELECT * FROM "+tableName+";\n";
        _for(
                sql,
                rs -> {
                    try {
                        ResultSetMetaData rsmd = rs.getMetaData();
                        int columnsNumber = rsmd.getColumnCount();
                        for (int i = 1; i <= columnsNumber; i++) {
                            System.out.print("| " + rsmd.getColumnName(i) + " |");
                        }
                    } catch (Exception e){}
                },
                rs -> {
            try {// loop through the result set
                while (rs.next()) {
                    System.out.print("\n");
                    for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                        String columnValue = rs.getString(i);
                        System.out.print("| " + columnValue + " |");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * SQL execution on connection!
     * @param sql
     */
    protected void _execute(String sql){
        Connection conn = _connection;
        try {
            Statement stmt = conn.createStatement();
            try {
                stmt.execute(sql);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Converts a ResultSet into a JSON Object.
     * It can be converted to a String and is sent
     * to the client when requested (Ajax).
     * @param rs
     * @return
     * @throws SQLException
     * @throws JSONException
     */
    protected static JSONArray _toJSON(ResultSet rs ) throws SQLException, JSONException
    {
        JSONArray json = new JSONArray();
        ResultSetMetaData rsmd = rs.getMetaData();

        while(rs.next()) {
            int numColumns = rsmd.getColumnCount();
            JSONObject jo = new JSONObject();

            for (int i=1; i<numColumns+1; i++)
            {
                String column_name = rsmd.getColumnName(i);

                if(rsmd.getColumnType(i)==java.sql.Types.ARRAY){
                    jo.put(column_name, rs.getArray(column_name));
                }
                else if(rsmd.getColumnType(i)==java.sql.Types.BIGINT){
                    jo.put(column_name, rs.getInt(column_name));
                }
                else if(rsmd.getColumnType(i)==java.sql.Types.BOOLEAN){
                    jo.put(column_name, rs.getBoolean(column_name));
                }
                else if(rsmd.getColumnType(i)==java.sql.Types.BLOB){
                    jo.put(column_name, rs.getBlob(column_name));
                }
                else if(rsmd.getColumnType(i)==java.sql.Types.DOUBLE){
                    jo.put(column_name, rs.getDouble(column_name));
                }
                else if(rsmd.getColumnType(i)==java.sql.Types.FLOAT){
                    jo.put(column_name, rs.getFloat(column_name));
                }
                else if(rsmd.getColumnType(i)==java.sql.Types.INTEGER){
                    jo.put(column_name, rs.getInt(column_name));
                }
                else if(rsmd.getColumnType(i)==java.sql.Types.NVARCHAR){
                    jo.put(column_name, rs.getNString(column_name));
                }
                else if(rsmd.getColumnType(i)==java.sql.Types.VARCHAR){
                    jo.put(column_name, rs.getString(column_name));
                }
                else if(rsmd.getColumnType(i)==java.sql.Types.TINYINT){
                    jo.put(column_name, rs.getInt(column_name));
                }
                else if(rsmd.getColumnType(i)==java.sql.Types.SMALLINT){
                    jo.put(column_name, rs.getInt(column_name));
                }
                else if(rsmd.getColumnType(i)==java.sql.Types.DATE){
                    jo.put(column_name, rs.getDate(column_name));
                }
                else if(rsmd.getColumnType(i)==java.sql.Types.TIMESTAMP){
                    jo.put(column_name, rs.getTimestamp(column_name));
                } else {
                    jo.put(column_name, rs.getObject(column_name));
                }
            }
            json.put(jo);
        }
        return json;
    }

    protected JSONArray _toCRUD(ResultSet rs, String mode) throws SQLException, JSONException
    {
        String[] tableNames = _listOfAllTables();

        JSONArray json = new JSONArray();
        ResultSetMetaData rsmd = rs.getMetaData();

        String tn = rsmd.getTableName(0);
        String relationTable = null;

        for(String t : tableNames){
            String[] words = t.split("_");
            boolean isRelationalTable = false;
            boolean isRelevant = false;
            for(String w : words) if(w.toLowerCase().contains("relation")) isRelationalTable = true;
            for(String w : words) if(w.toLowerCase().contains(tn)) isRelevant = true;
            if (isRelationalTable && isRelevant && words.length==2) {
                relationTable = t;
            }
        }

        while(rs.next()) {
            int numColumns = rsmd.getColumnCount();
            JSONObject obj = new JSONObject();

            for (int i=1; i<numColumns+1; i++)
            {
                String column_name = rsmd.getColumnName(i);

                if(rsmd.getColumnType(i)==java.sql.Types.ARRAY){
                    obj.put(column_name, rs.getArray(column_name));
                }
                else if(rsmd.getColumnType(i)==java.sql.Types.BIGINT){
                    obj.put(column_name, rs.getInt(column_name));
                }
                else if(rsmd.getColumnType(i)==java.sql.Types.BOOLEAN){
                    obj.put(column_name, rs.getBoolean(column_name));
                }
                else if(rsmd.getColumnType(i)==java.sql.Types.BLOB){
                    obj.put(column_name, rs.getBlob(column_name));
                }
                else if(rsmd.getColumnType(i)==java.sql.Types.DOUBLE){
                    obj.put(column_name, rs.getDouble(column_name));
                }
                else if(rsmd.getColumnType(i)==java.sql.Types.FLOAT){
                    obj.put(column_name, rs.getFloat(column_name));
                }
                else if(rsmd.getColumnType(i)==java.sql.Types.INTEGER){
                    obj.put(column_name, rs.getInt(column_name));
                }
                else if(rsmd.getColumnType(i)==java.sql.Types.NVARCHAR){
                    obj.put(column_name, rs.getNString(column_name));
                }
                else if(rsmd.getColumnType(i)==java.sql.Types.VARCHAR){
                    obj.put(column_name, rs.getString(column_name));
                }
                else if(rsmd.getColumnType(i)==java.sql.Types.TINYINT){
                    obj.put(column_name, rs.getInt(column_name));
                }
                else if(rsmd.getColumnType(i)==java.sql.Types.SMALLINT){
                    obj.put(column_name, rs.getInt(column_name));
                }
                else if(rsmd.getColumnType(i)==java.sql.Types.DATE){
                    obj.put(column_name, rs.getDate(column_name));
                }
                else if(rsmd.getColumnType(i)==java.sql.Types.TIMESTAMP){
                    obj.put(column_name, rs.getTimestamp(column_name));
                }
                else{
                    obj.put(column_name, rs.getObject(column_name));
                }
                if(relationTable!=null && obj.get("id")!=null){
                    Object id = obj.get("id");
                    String sql = "SELECT * FROM "+relationTable+" rt "+" WHERE rt.child_tails_id = "+id.toString();
                    _for(sql, cs->{
                        try {
                            obj.put("children", _toCRUD(cs, mode));
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                    }, null);

                }
            }
            json.put(obj);
        }
        return json;
    }

    /**
     * Converts a ResultSet into a Document data structure.
     * This later on used to generate XML!
     * @param rs This is a ResultSet fetched from a Database.
     * @return
     * @throws ParserConfigurationException
     * @throws SQLException
     */
    protected static Document toDocument(ResultSet rs)
            throws ParserConfigurationException, SQLException
    {
        //Define a new Document object
        Document dataDoc = null;
        try {
            //Create the DocumentBuilderFactory
            DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
            //Create the DocumentBuilder
            DocumentBuilder docbuilder = dbfactory.newDocumentBuilder();
            //Instantiate a new Document object
            dataDoc = docbuilder.newDocument();
        } catch (Exception e) {
            System.out.println("Problem creating document: "+e.getMessage());
        }
        ResultSetMetaData resultmetadata = rs.getMetaData();
        //Create a new element called "data"
        Element dataRoot = dataDoc.createElement("data");
        int numCols = resultmetadata.getColumnCount();
        while (rs.next()) {
            //For each row of data
            //Create a new element called "row"
            Element rowEl = dataDoc.createElement("row");
            for (int i=1; i <= numCols; i++) {
                //For each column, retrieve the name and data
                String colName = resultmetadata.getColumnName(i);
                String colVal = rs.getString(i);
                //If there was no data, add "and up"
                if (rs.wasNull()) {
                    colVal = "and up";
                }
                //Create a new element with the same name as the column
                Element dataEl = dataDoc.createElement(colName);
                //Add the data to the new element
                dataEl.appendChild(dataDoc.createTextNode(colVal));
                //Add the new element to the row
                rowEl.appendChild(dataEl);
            }
            //Add the row to the root element
            dataRoot.appendChild(rowEl);
        }
        //Add the root element to the document
        dataDoc.appendChild(dataRoot);
        return dataDoc;
    }



    protected void _executeFile(String name){
        Connection conn = _connection;
        String[] commands;
        File file = new File("db/", name);
        int fileLength = (int) file.length();
        try {
            byte[] fileData = IPlugin.util.readFileData(file, fileLength);
            String query = new String(fileData);
            commands = query.split("--<#SPLIT#>--");
            for(String command : commands){
                _execute(command);
            }
            try {
                conn.commit();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}

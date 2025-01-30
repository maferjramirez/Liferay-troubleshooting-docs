// SQL query and limit settings
SQL = "SELECT * FROM company"
LIMIT = 10

import java.io.PrintStream
import com.liferay.portal.kernel.dao.jdbc.DataAccess
import com.liferay.portal.kernel.util.HtmlUtil

// Function to format the result as CSV
def formatAsCSV(table) {
    table.collect { row -> row.join(",") }.join("\n")
}

// Function to format the result as HTML
def formatAsHTML(table) {
    def header = "<tr style='background-color: #f2f2f2; font-weight: bold; text-align: left;'>" + 
                 table[0].collect { "<th style='padding: 8px; border-bottom: 2px solid #ddd;'>${it}</th>" }.join("") + 
                 "</tr>"
    
    def rows = ""
    table.tail().each { row ->
        rows += "<tr style='border-bottom: 1px solid #ddd;'>" + 
                row.collect { "<td style='padding: 8px;'>${HtmlUtil.escape(it.toString())}</td>" }.join("") + 
                "</tr>\n"
    }
    
    return """
        <table style='width: 100%; border-collapse: collapse; margin: 10px 0;'>
        <thead>${header}</thead>
        <tbody>${rows}</tbody>
        </table>
    """
}

// Function to format the result as JSON
def formatAsJSON(table) {
    if (table.isEmpty()) return "[]"
    
    def headers = table[0]
    def jsonRows = table.tail().collect { row ->
        headers.indices.collectEntries { index ->
            def value = row[index]
            def jsonValue = value == null ? 'null' : 
                           (value instanceof Number || value instanceof Boolean) ? value : 
                           "\"${escapeJSONString(value.toString())}\""
                           
            [(headers[index]): jsonValue]
        }
    }.collect { rowMap ->
        "{" + rowMap.entrySet().collect { 
            "\"${escapeJSONString(it.key)}\":${it.value}" 
        }.join(",") + "}"
    }
    
    "[${jsonRows.join(",")}]"
}

// Escapes special characters in strings to comply with the JSON standard.
def escapeJSONString(String str) {
    str.replaceAll("\\\\", "\\\\\\\\")
       .replaceAll("\"", "\\\\\"")
       .replaceAll("\b", "\\\\b")
       .replaceAll("\f", "\\\\f")
       .replaceAll("\n", "\\\\n")
       .replaceAll("\r", "\\\\r")
       .replaceAll("\t", "\\\\t")
}

// Mapping of output formats
def formatters = [
    "CSV": this.&formatAsCSV,
    "HTML": this.&formatAsHTML,
    "JSON": this.&formatAsJSON
]

// Function to execute SQL query and fetch results
def executeQuery(query) {
    def resultTable = []
    def connection, statement, resultSet
    try {
        connection = DataAccess.getConnection()
        statement = connection.createStatement()
        statement.setMaxRows(LIMIT)
        resultSet = statement.executeQuery(query)
        
        def metadata = resultSet.getMetaData()
        def columnCount = metadata.getColumnCount()
        
        resultTable << (1..columnCount).collect { metadata.getColumnLabel(it) }
        
        while (resultSet.next()) {
            resultTable << (1..columnCount).collect {
                metadata.getColumnTypeName(it) == "CLOB" ? resultSet.getClob(it).getSubString(1, resultSet.getClob(it).length()) : resultSet.getObject(it)
            }
        }
    } catch (Exception e) {
        e.printStackTrace(System.out)
    } finally {
        DataAccess.cleanUp(connection, statement, resultSet)
    }
    return resultTable
}

// Execute query and print formatted output
def queryResult = executeQuery(SQL)
println formatters["JSON"](queryResult)

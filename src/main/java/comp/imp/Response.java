package comp.imp;

import comp.IResponse;

import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class Response implements IResponse {

    Map<String, String> _headers = new HashMap<>();
    String _content_type = "";
    int _status_code = -1;
    byte[] _content;

    @Override
    public Map<String, String> getHeaders() {
        return _headers;
    }

    @Override
    public int getContentLength() {
        return (_content!=null)?_content.length:0;
    }

    @Override
    public String getContentType() {
        return _content_type;
    }

    @Override
    public void setContentType(String contentType) {
        _content_type = contentType;
    }

    @Override
    public int getStatusCode() {
        if(_status_code==-1){
            throw new RuntimeException("[RESPONSE]: ERROR. Status code not set!");
        }
        return _status_code;
    }

    @Override
    public void setStatusCode(int status) {
        _status_code = status;
    }

    @Override
    public String getStatus() {
        if(_status_code==-1){
            throw new RuntimeException("[RESPONSE]: ERROR. Status code not set!");
        }
        String code = String.valueOf(_status_code);
        switch(_status_code){
            case 200: return code+"ok";
            case 404: return code+"notfound";
            case 500: return code+"internalservererror";
        }
        return code;
    }

    @Override
    public void addHeader(String header, String value) {
        _headers.put(header, value);
    }

    @Override
    public String getServerHeader() {
        return _headers.get("core.WebioServer");
    }

    @Override
    public void setServerHeader(String server) {
        _headers.put("core.WebioServer", server);
    }

    @Override
    public void setContent(String content) {
        try {
            _content = content.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setContent(byte[] content) {
        _content = content;
    }

    @Override
    public void setContent(InputStream stream) {//_socket.getStream()...
        BufferedReader in = new BufferedReader(new InputStreamReader(stream));
        //stream.readAllBytes();
        // get first line of the request from the client
        String input = null;
        try {
            input = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // we parse the request with a string tokenizer
        StringTokenizer parse = new StringTokenizer(input);
        String method = parse.nextToken().toUpperCase(); // we get the HTTP method of the client
        _headers.put("GET", method);
        while(parse.hasMoreTokens()){
            _headers.put(parse.toString(), parse.nextToken());
        }
        // we get file requested
        String fileRequested = parse.nextToken().toLowerCase();
        System.out.println("File: "+fileRequested);
    }

    private String _getHeaderString(){
        String[] header = {""};
        _headers.forEach((k, v)->{
            header[0] += k+": "+v+"\n";

        });
        header[0]+="\n";
        return header[0];
    }

    @Override
    public void send(OutputStream network)
    {
        if(getContentLength()==0 && getContentType().equals("text/html")){
           throw new RuntimeException("[RESPONSE]: Sending failed! Content of type text/html is empty!");
        }
        PrintWriter out = new PrintWriter(network);
        OutputStream dataOut = network;//new BufferedOutputStream(network);
        byte[] fileData = _content;// send HTTP Headers

        String header = "HTTP/1.1 "+getStatus()+"\n"+
                        "core.WebioServer: Webio!\n"+
                        "Date: " + new Date()+"\n"+
                        "Content-type: " + "text/html\n"+
                        "Content-length: " + getContentLength()+"\n\n";
        header = _getHeaderString();
        if(!header.contains("HTTP/1.") || !header.substring(0, 7).equals("HTTP/1.")){
            header = "HTTP/1. "+getServerHeader()+" "+getStatus()+header;
        }



        byte[] headerBytes = new byte[0];
        try {
            headerBytes = header.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try{
        dataOut.write(headerBytes, 0, headerBytes.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(_content!=null) {
            try {
                //out.println(new String(_content));
                dataOut.write(fileData, 0, getContentLength());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            dataOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString(){
        return  "[Response]:(S-"+this.getStatus()+", "+"C-"+this._content_type+", L-"+this.getContentLength()+"); ";
    }

}

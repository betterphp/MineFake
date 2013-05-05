package uk.co.jacekk.minefake;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class QueryReply {
	
	private ByteArrayOutputStream buffer;
	private DataOutputStream stream;
	
	public QueryReply(int i){
		this.buffer = new ByteArrayOutputStream(i);
		this.stream = new DataOutputStream(this.buffer);
	}
	
	public void write(byte[] value) throws IOException {
		this.stream.write(value, 0, value.length);
	}
	
	public void write(String value) throws IOException {
		this.stream.writeBytes(value);
		this.stream.write(0);
	}
	
	public void write(int value) throws IOException {
		this.stream.write(value);
	}
	
	public void write(short value) throws IOException {
		this.stream.writeShort(Short.reverseBytes(value));
	}
	
	public byte[] getBytes(){
		return this.buffer.toByteArray();
	}
	
	public void reset(){
		this.buffer.reset();
	}
	
}

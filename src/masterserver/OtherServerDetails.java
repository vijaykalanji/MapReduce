package masterserver;

import java.io.Serializable;
import java.util.List;

public class OtherServerDetails implements Serializable{
	String name;
	int port;
	int serverNum;
	List<Integer> partitionList;

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getServerNum() {
		return serverNum;
	}

	public void setServerNum(int serverNum) {
		this.serverNum = serverNum;
	}

	public List<Integer> getPartitionList() {
		return partitionList;
	}

	public void setPartitionList(List<Integer> partitionList) {
		this.partitionList = partitionList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}

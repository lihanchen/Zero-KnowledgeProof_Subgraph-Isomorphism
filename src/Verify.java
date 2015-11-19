import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class Verify {

	public static void main(String[] args) throws IOException {

		String serverAddress = "127.0.0.1";
		Socket socket = new Socket(serverAddress, 8080);
		try {
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

			String g = (String) ois.readObject();
			System.out.println(g);

			String g2 = (String) ois.readObject();
			System.out.println(g2);

			String hashofSubgraphPrime = (String) ois.readObject();
			System.out.println(hashofSubgraphPrime);

			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			int randomNum = (int) (Math.random() + 0.5);
			out.println(randomNum);
			int result = 1;
			if (randomNum == 1) {
				Graph gG = new Graph(g);
				String q = (String) ois.readObject();
				HashMap<Integer, Integer> iso = (HashMap<Integer, Integer>) ois.readObject();
				Graph qG = new Graph(q);
				if (!new String(qG.hash(), StandardCharsets.UTF_8).equals(hashofSubgraphPrime)) {
					System.out.println("test1 fail");
					System.out.println(new String(qG.hash(), StandardCharsets.UTF_8));
					result = 0;
				}
				Graph testQ = gG.isomorphism(iso);
				if (!testQ.equals(qG)) {
					System.out.println("test2 fail");
					System.out.println(testQ.toString());
					System.out.println(qG.toString());
					result = 0;
				}
			} else {
				String subgraphPrime = (String) ois.readObject();
				HashMap<Integer, Integer> iso = (HashMap<Integer, Integer>) ois.readObject();
				Graph g2G = new Graph(g2);
				Graph qPrime = new Graph(subgraphPrime);
				Graph testg2G = qPrime.isomorphism(iso);
				if (!testg2G.equals(g2G)) {
					System.out.println("test3 fail");
					System.out.println(testg2G.toString());
					System.out.println(g2G.toString());
					result = 0;
				}
			}
			out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			socket.close();
		}
	}
}

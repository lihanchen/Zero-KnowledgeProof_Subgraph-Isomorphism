import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Verify {

	public static void main(String[] args) throws IOException {

		String serverAddress = args[0];
		int roundNum = Integer.parseInt(args[1]);
		Socket socket = new Socket(serverAddress, 8080);
		try {
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

			Graph g = (Graph) ois.readObject();
			Graph g2 = (Graph) ois.readObject();

			oos.writeInt(roundNum);
			oos.flush();

			ArrayList<byte[]> grhash = (ArrayList<byte[]>) ois.readObject();
			ArrayList<byte[]> grprimehash = (ArrayList<byte[]>) ois.readObject();

			boolean[] challenges = new boolean[roundNum];
			for (int i = 0; i < roundNum; i++)
				challenges[i] = Math.random() > 0.5;
			oos.writeObject(challenges);
			oos.flush();

			ArrayList<HashMap<Integer, Integer>> changes = (ArrayList<HashMap<Integer, Integer>>) ois.readObject();
			ArrayList<Graph> graph = (ArrayList<Graph>) ois.readObject();

			for (int i = 0; i < roundNum; i++) {
				if (challenges[i]) {
					Graph gr = g2.isomorphism(changes.get(i));
					if (!Arrays.equals(gr.hash(), grhash.get(i))) {
						System.out.println("Verification Failed in Round " + i);
						throw new Exception("Verification Exception");
					}
				} else {
					Graph grprime = graph.get(i);
					if (!Arrays.equals(grprime.hash(), grprimehash.get(i)) || !grprime.isomorphism(changes.get(i)).equals(g)) {
						System.out.println("Verification Failed in Round " + i);
						throw new Exception("Verification Exception");
					}
				}
				System.out.println("Verification Successful in Round " + i);
			}

			System.out.println("Verification Successful");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Verification Failed");
		}
	}
}

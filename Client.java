import java.io.*;
import java.net.*;

public class Client {
	static char[] square = { 'o', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

	public static int checkwin() {
		if (square[1] == square[2] && square[2] == square[3])
			return 1;
		else if (square[4] == square[5] && square[5] == square[6])
			return 1;
		else if (square[7] == square[8] && square[8] == square[9])
			return 1;
		else if (square[1] == square[4] && square[4] == square[7])
			return 1;
		else if (square[2] == square[5] && square[5] == square[8])
			return 1;
		else if (square[3] == square[6] && square[6] == square[9])
			return 1;
		else if (square[1] == square[5] && square[5] == square[9])
			return 1;
		else if (square[3] == square[5] && square[5] == square[7])
			return 1;
		else if (square[1] != '1' && square[2] != '2' && square[3] != '3' &&
				square[4] != '4' && square[5] != '5' && square[6] != '6' && square[7] != '7' && square[8] != '8'
				&& square[9] != '9')
			return 0;
		else
			return -1;
	}

	public static void board() {
		System.out.println("\n\n\tTic Tac Toe\n\n");
		System.out.println("CLIENT (X)  -  SERVER (O)\n\n\n");
		System.out.println("     |     |     ");
		System.out.println("  " + square[1] + "  |  " + square[2] + "  |  " + square[3] + " ");
		System.out.println("_____|_____|_____");
		System.out.println("     |     |     ");
		System.out.println("  " + square[4] + "  |  " + square[5] + "  |  " + square[6] + " ");
		System.out.println("_____|_____|_____");
		System.out.println("     |     |     ");
		System.out.println("  " + square[7] + "  |  " + square[8] + "  |  " + square[9] + " ");
		System.out.println("     |     |     \n\n");
	}

	public static void main(String[] args) {
		try {
			Socket s = new Socket("localhost", 8080);
			System.out.println("Connected to server...");

			DataInputStream dis = new DataInputStream(s.getInputStream());
			DataOutputStream dos = new DataOutputStream(s.getOutputStream());

			int i = checkwin();
			while (i != 1) {
				board();
				System.out.println("CLIENT Please Enter Your Choice\n");
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				int choice = Integer.parseInt(br.readLine());

				if (!(choice >= 1 && choice <= 9)) {
					System.out.println("Invalid move\n");
					System.out.println("Please enter a valid choice between 1-9\n");
					choice = Integer.parseInt(br.readLine());
				}

				char mark = 'X';
				if (choice >= 1 && choice <= 9 && square[choice] == (char) (choice + '0')) {
					square[choice] = mark;
				}

				dos.writeInt(choice);
				int serverChoice = dis.readInt();

				if (serverChoice == 10) {
					System.out.println("You win");
					s.close();
					break;
				}

				mark = 'O';
				if (serverChoice >= 1 && serverChoice <= 9 && square[serverChoice] == (char) (serverChoice + '0')) {
					square[serverChoice] = mark;
				}

				board();
				i = checkwin();
				System.out.println("\n" + i + "\n");
				if (i == 1) {
					board();
					int flag = 11;
					System.out.println("Server wins");
					dos.writeInt(flag);
					s.close();
					break;
				}
			}

			s.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
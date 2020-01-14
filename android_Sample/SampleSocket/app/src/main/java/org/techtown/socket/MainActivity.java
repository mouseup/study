package org.techtown.socket;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    EditText editText;

    TextView textView;
    TextView textView2;

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
        textView = findViewById(R.id.textView);
        textView2 = findViewById(R.id.textView2);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String data = editText.getText().toString();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // 스레드 안에서 send 메서드 호출
                        send(data);
                    }
                }).start();

            }
        });

        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // 스레드 안에서 startServer 메서드 호출
                        startServer();
                    }
                }).start();

            }
        });
    }

    public void send(String data) {
        try {
            int portNumber = 5001;
            // 소켓 객체 만들기 - localhost 자기자신
            Socket sock = new Socket("localhost", portNumber);
            printClientLog("소켓 연결함.");

            // 소켓 객체로 데이터 보내기
            ObjectOutputStream outstream = new ObjectOutputStream(sock.getOutputStream());
            outstream.writeObject(data);
            outstream.flush();
            printClientLog("데이터 전송함.");

            // 서버에서 받기
            ObjectInputStream instream = new ObjectInputStream(sock.getInputStream());
            printClientLog("서버로부터 받음 : " + instream.readObject());
            sock.close();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public void startServer() {
        try {
            // 클라이언트가 이 포트 번호로 접속해야함
            int portNumber = 5001;

            // 소켓 서버 객체 만들기
            ServerSocket server = new ServerSocket(portNumber);
            printServerLog("서버 시작함 : " + portNumber);

            // while 구문을 사용해서 클라이언트의 접속을 기다리다가 클라이언트의 접속 요청이 왔을때
            // accept 메서드를 통해 소켓 객체가 반환되므로 클라이언트 소켓의 연결 정보를 확인할수 있다.
            while(true) {
                // 서버 대기상태 -> 클라이언트가 접속을 하면 sock 이라는 객체 리턴
                Socket sock = server.accept();
                InetAddress clientHost = sock.getLocalAddress();
                int clientPort = sock.getPort();
                printServerLog("클라이언트 연결됨 : " + clientHost + " : " + clientPort);

                // ObjectOutputStream 객체 생성
                ObjectInputStream instream = new ObjectInputStream(sock.getInputStream());
                // 클라이언트에서 보내온 객체를 읽기
                Object obj = instream.readObject();
                printServerLog("데이터 받음 : " + obj);

                // 클라이언트로 받은 데이타 보내기
                ObjectOutputStream outstream = new ObjectOutputStream(sock.getOutputStream());
                outstream.writeObject(obj + " from Server.");
                // 버퍼에 남아있을수 있기 때문에 flush
                outstream.flush();
                printServerLog("데이터 보냄.");

                // 소켓 끊기 - 리소스 절약
                sock.close();
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public void printClientLog(final String data) {
        Log.d("MainActivity", data);

        handler.post(new Runnable() {
            @Override
            public void run() {
                textView.append(data + "\n");
            }
        });

    }

    public void printServerLog(final String data) {
        Log.d("MainActivity", data);

        handler.post(new Runnable() {
            @Override
            public void run() {
                textView2.append(data + "\n");
            }
        });
    }

}

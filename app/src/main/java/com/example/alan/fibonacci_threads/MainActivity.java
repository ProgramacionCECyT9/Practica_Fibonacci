package com.example.alan.fibonacci_threads;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    private EditText inputNumber;
    private TextView outputView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inputNumber = (EditText) findViewById(R.id.numberTextField);
        outputView = (TextView) findViewById(R.id.resultTextView);
    }

    public void getFibonnaciNumber(View view) {
        int n = Integer.parseInt(inputNumber.getText().toString());
        if(n < 1) {
            outputView.append("No es posible calcular posiciones negativas o la posicion 0 de la serie de fibonnaci.\n");
        }
        else {
            outputView.append("Fibonacci[" +n + "] = ");
            FibonacciOperationThread fibonacciThread = new FibonacciOperationThread();
            fibonacciThread.execute(n);
        }
    }

    class FibonacciOperationThread extends AsyncTask<Integer, Integer, Integer> {

        private ProgressDialog progressDialog;

        @Override protected void onPreExecute() {

            progressDialog = new ProgressDialog(MainActivity.this);

            progressDialog.setProgressStyle(ProgressDialog.
                    STYLE_HORIZONTAL);

            progressDialog.setMessage("Calculando...");

            progressDialog.setCancelable(true);

            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

                @Override
                public void onCancel(DialogInterface dialog) {

                    FibonacciOperationThread.this.cancel(true);

                }

            });

            progressDialog.setMax(100);

            progressDialog.setProgress(0);

            progressDialog.show();

        }

        @Override protected Integer doInBackground(Integer... n) {

            int result = 1;
            int n1 = 1, n2 = 1;
            int fibonacciPosition = n[0];

            if(fibonacciPosition == 1) {
                return 0;
            }
            else if(fibonacciPosition == 2 || fibonacciPosition == 3) {
                return 1;
            }
            else {
                for (int i = 4; i <= fibonacciPosition && !isCancelled(); i++) {
                    n2 = n1;
                    n1 = result;
                    result = n1 + n2;

                    SystemClock.sleep(150);

                    publishProgress(i*100 / n[0]);

                }
                return result;
            }
        }

        @Override protected void onProgressUpdate(Integer... porc) {

            progressDialog.setProgress(porc[0]);

        }

        @Override protected void onPostExecute(Integer res) {

            progressDialog.dismiss();

            outputView.append(res + "\n");

        }

        @Override protected void onCancelled() {

            outputView.append("Cancelado.\n");

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

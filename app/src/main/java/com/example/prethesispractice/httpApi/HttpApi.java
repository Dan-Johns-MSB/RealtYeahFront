package com.example.prethesispractice.httpApi;

import android.content.Context;

import com.example.prethesispractice.R;
import com.example.prethesispractice.app.App;

import java.io.InputStream;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttp;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.tls.HandshakeCertificates;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpApi {
    public static final String BASE_URL = "https://192.168.0.102:45455/";
    Retrofit retrofit;
    private final Context context;

    public HttpApi() {
        //App app = new App();
        context = App.getContext();
        InputStream caInput = context.getResources().openRawResource(R.raw.conveyor);

        Certificate ca;
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            ca = cf.generateCertificate(caInput);
            caInput.close();
            HandshakeCertificates certificates = new HandshakeCertificates.Builder()
                    .addTrustedCertificate((X509Certificate) ca)
                    .addPlatformTrustedCertificates()
                    .build();


            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .sslSocketFactory(certificates.sslSocketFactory(), certificates.trustManager()/* trustAllCerts[0]*/)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .addInterceptor(logging)
                    .build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Retrofit getRetrofit() {
        return this.retrofit;
    }
}

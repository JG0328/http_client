package edu.pucmm.httpclient;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Digite una URL valida: ");
        String myUrl = scanner.nextLine();
        System.out.println("La URL es: " + myUrl);

        try {
            Document doc = Jsoup.connect(myUrl).get();

            // Contar la cantidad de lineas

            // Contar la cantidad de parrafos
            Elements ps = doc.select("p");
            System.out.println("Total de parrafos: " + ps.size());

            // Contar la cantidad de imagenes dentro de parrafos
            Elements imgs = doc.select("p").select("img");
            System.out.println("Total de imagenes dentro de parrafos: " + imgs.size());

            // Clasificando los forms por metodos GET y POST
            Elements forms = doc.select("form");
            System.out.println("Total de formularios: " + forms.size());
            System.out.println();

            int getForms = 0, postForms = 0;

            for (Element f : forms) {
                if (f.attr("method").equalsIgnoreCase("get")) {
                    getForms++;
                } else if (f.attr("method").equalsIgnoreCase("post")) {
                    postForms++;
                }
            }

            System.out.println("GET: " + getForms);
            System.out.println("POST: " + postForms);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

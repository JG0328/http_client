package edu.pucmm.httpclient;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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
            String docParse = Jsoup.connect(myUrl).execute().body();
            System.out.println("\nTotal de lineas: " + getLines(docParse));

            // Contar la cantidad de parrafos

            System.out.println("Total de parrafos: " + getParagraphs(doc));

            // Contar la cantidad de imagenes dentro de parrafos
            System.out.println("Total de imagenes dentro de parrafos: " + getImagesInParagraphs(doc));

            // Clasificando los forms por metodos GET y POST
            Elements forms = doc.select("form");
            System.out.println("\nTotal de formularios: " + forms.size());
            getDifferentForms(forms);

            // Obteniendo los input de cada form
            getInputsInForms(forms);

            // Enviar peticion
            sendRequest(forms, myUrl);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int getLines(String str) {
        return str.split("\n").length;
    }

    private static int getParagraphs(Document doc) {
        Elements ps = doc.select("p");
        return ps.size();
    }

    private static int getImagesInParagraphs(Document doc) {
        Elements imgs = doc.select("p").select("img");
        return imgs.size();
    }

    private static void getDifferentForms(Elements forms) {
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

        System.out.println();
    }

    private static void getInputsInForms(Elements forms) {
        for (int i = 0; i < forms.size(); i++) {
            System.out.println("Form #" + (i + 1));
            Elements inputs = forms.get(i).select("input");
            System.out.println("Tiene " + inputs.size() + " input");
            for (int j = 0; j < inputs.size(); j++) {
                System.out.println("Input #" + (j + 1) + ", type: " + inputs.get(j).attr("type"));
            }
            System.out.println();
        }
    }

    private static void sendRequest(Elements forms, String myUrl) {
        int i = 0;
        for (Element form : forms) {
            if (form.attr("method").equalsIgnoreCase("post")) {
                String url = "";

                if (myUrl.contains("https")) {
                    url = myUrl;
                } else {
                    String protocol = myUrl.substring(0, 4);
                    if (protocol.equalsIgnoreCase("https")) {
                        String tmp = myUrl.substring(8);
                        String[] tmpUrl = tmp.split("/");
                        url = tmpUrl[0] + form.attr("action");
                    } else {
                        String tmp = myUrl.substring(7);
                        String[] tmpUrl = tmp.split("/");
                        url = protocol + "://" + tmpUrl[0] + form.attr("action");
                    }
                }

                try {
                    Document request = Jsoup.connect(url).header("matricula", "20160290").data("asignatura", "practica1").post();
                    System.out.println("Respuesta Form#" + (i + 1) + ": ");
                    System.out.println(request.html());
                    System.out.println();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            i++;
        }
    }
}

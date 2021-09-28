package com.pucrs.vv2;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * 
 * Automação de Testes com Selenium WebDriver - Micro exemplo
 * Prof. Daniel Callegari
 * VV2 - PUCRS
 * 
 * Antes de executar este código, você deve:
 * 1. Baixar o WebDriver do Chrome em https://sites.google.com/a/chromium.org/chromedriver/downloads
 * 2. Descompactar e colocar em um diretório à sua escolha;
 * 3. Configurar o local do driver alterando a constante 'driverDoChrome' no código da classe.
 * 
 * Atenção:
 *  Este exemplo não segue as melhores práticas de uso do Selenium WebDriver para testes funcionais,
 *  sendo apenas uma rápida demonstração de algumas de suas funcionalidades.
 * 
 */
public class App 
{
    // Configure aqui o local onde você baixou o WebDriver do Chrome na sua máquina:
    private static final String driverDoChrome = "C:\\dev\\WebDriver\\chromedriver.exe";

    public static void main(final String[] args) {
        /**
         * Exemplo 1
         * 1. Abre o site do Google
         * 2. Digita "20 + 22" no campo de busca
         * 3. Pressiona ENTER para acionar a função de calculadora do Google
         * 4. Espera a página de resultados
         * 5. Verifica se o resultado é 42.
         */
        //executarExemplo1();

        /**
         * Exemplo 2
         * 1. Abre o site da biblioteca da PUCRS
         * 2. Digita "Java" no campo de busca
         * 3. Pressiona ENTER para iniciar a busca (o sistema abre outra aba com os resultados)
         * 4. [Espera carregar a nova aba]
         * 5. [Muda para a nova aba (procurando por uma diferente da atual!)]
         * 6. Aguarda a página de resultados carregar
         * 7. Busca por ocorrências de resultados
         * 8. Verifica se pelo menos um deles contém a string buscada
         */
        executarExemplo2();
    }


    // teste
    
    ////////////////////////////////////////////////////////////////////////////////////////////////
    public static void executarExemplo1 () {
        final boolean fecharBrowserNoFinal = false;
        boolean esteTestePassou = false;
        System.out.println("+++++++++++++++ Iniciando Exemplo 1...");

        // Propriedades Gerais
        System.setProperty("webdriver.chrome.driver", driverDoChrome);

        // Configurações do Chrome
        final ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);

        // Instancia o WebDriver do Chrome
        final WebDriver driver = new ChromeDriver();

        try {
            // Usa a calculadora do Google
            driver.get("https://google.com");
            WebElement caixaDeBusca = driver.findElement(By.name("q"));
            caixaDeBusca.sendKeys("20 + 22");
            caixaDeBusca.sendKeys(Keys.ENTER);

            WebDriverWait wait1 = new WebDriverWait(driver, 10);
            wait1.until(ExpectedConditions.visibilityOfElementLocated(By.id("cwos")));

            WebElement elementoResultado = driver.findElement(By.id("cwos"));
            String strResult = elementoResultado.getText();
            int iResult = Integer.parseInt(strResult);

            esteTestePassou = (iResult == 45);

        }
        catch (Exception e) {
            esteTestePassou = false;
            System.err.println(e.toString());
        } finally {
            if (fecharBrowserNoFinal) {
                driver.quit();
            }
        }

        System.out.println(">>>>>>>>>>>>>>> Resultado do teste: " + ((esteTestePassou)? "PASSOU" : "FALHOU"));
        System.out.println("+++++++++++++++ Fim do exemplo 1.");
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    public static void executarExemplo2 () {
        final boolean fecharBrowserNoFinal = true;
        boolean esteTestePassou = false;

        System.out.println("+++++++++++++++ Iniciando Exemplo 2...");

        // Propriedades Gerais
        System.setProperty("webdriver.chrome.driver", driverDoChrome);

        // Configurações do Chrome
        final ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);

        // Instancia o WebDriver do Chrome
        final WebDriver driver = new ChromeDriver();

        try {

            String dadosDePesquisa = "Python";

            driver.get("http://primo-pmtna01.hosted.exlibrisgroup.com/primo_library/libweb/action/search.do?vid=PUC01");
            driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);

            // Título da Página
            final String tituloPagina = driver.getTitle();
            System.out.println("Chegou na página: " + tituloPagina);
            String paginaDeBusca = driver.getWindowHandle();

            // Caixa de Busca do Sistema OMNIS da Biblioteca da PUCRS
            final WebElement caixaDeBusca = driver.findElement(By.id("search_field"));
            //caixaDeBusca.sendKeys("Daniel Antonio Callegari");
            caixaDeBusca.sendKeys(dadosDePesquisa);
            caixaDeBusca.sendKeys(Keys.ENTER); 
            
            // O Sistema OMNIS abre uma nova aba, entao..
            // Espera carregar a nova página
            /*
            WebDriverWait wait1 = new WebDriverWait(driver, 10);
            wait1.until(ExpectedConditions.numberOfWindowsToBe(2));
            //Procura a nova aba e vai para ela
            for (String windowHandle : driver.getWindowHandles()) {
                if(!paginaDeBusca.contentEquals(windowHandle)) {
                    driver.switchTo().window(windowHandle);
                    break;
                }
            }
            */
            System.out.println("Aguardando a página de resultados...");
            // Aguarda a página de resultados carregar
            WebDriverWait wait2 = new WebDriverWait(driver, 10);
            WebElement espera2 = wait2.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Exibir On-line")));
            System.out.println("A página de resultados terminou de carregar.");

            System.out.println("Buscando as obras resultantes...");
            // Busca por ocorrências de resultados
            final List<WebElement> obras = driver.findElements(By.className("EXLResultTitle"));
            System.out.printf("Foram encontradas %d obras:\n", obras.size());
            System.out.println("===================================================");
            int nroObra = 1;
            for (final WebElement e : obras) {
                String txtObra = e.getText().replaceAll("\n", ", "); // remove eventuais quebras de linha

                // Algum resultado corresponde à pesquisa?
                if (txtObra.toUpperCase().contains(dadosDePesquisa.toUpperCase())) esteTestePassou = true;

                System.out.printf("OBRA %02d: '%s'\n", nroObra++, txtObra);
            }
            System.out.println("===================================================");
            
    
        }
        catch (Exception e) {
            esteTestePassou = false;
            System.err.println(e.toString());
        } finally {
            if (fecharBrowserNoFinal) {
                driver.quit();
            }
        }

        System.out.println(">>>>>>>>>>>>>>> Resultado do teste: " + ((esteTestePassou)? "PASSOU" : "FALHOU"));
        System.out.println("+++++++++++++++ Fim do exemplo 2.");
    }


}

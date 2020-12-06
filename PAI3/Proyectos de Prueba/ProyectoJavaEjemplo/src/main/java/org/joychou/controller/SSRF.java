package org.joychou.controller;

import org.joychou.security.SecurityUtil;
import org.joychou.security.ssrf.SSRFException;
import org.joychou.util.HttpUtils;
import org.joychou.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.*;


/**
 * Java SSRF vuln or security code.
 *
 * @author JoyChou @2017-12-28
 */

@RestController
@RequestMapping("/ssrf")
public class SSRF {

    private static Logger logger = LoggerFactory.getLogger(SSRF.class);


    /**
     * The default setting of followRedirects is true.
     * UserAgent is Java/1.8.0_102.
     */
    @RequestMapping(value = "/urlConnection/vuln", method = {RequestMethod.POST, RequestMethod.GET})
    public String URLConnectionVuln(String url) {
        return HttpUtils.URLConnection(url);
    }


    @GetMapping("/urlConnection/sec")
    public String URLConnectionSec(String url) {

        // Decline not http/https protocol
        if (!SecurityUtil.isHttp(url)) {
            return "[-] SSRF check failed";
        }

        try {
            SecurityUtil.startSSRFHook();
            return HttpUtils.URLConnection(url);
        } catch (SSRFException | IOException e) {
            return e.getMessage();
        } finally {
            SecurityUtil.stopSSRFHook();
        }

    }


    /**
     * The default setting of followRedirects is true.
     * UserAgent is Java/1.8.0_102.
     */
    @GetMapping("/HttpURLConnection/sec")
    public String httpURLConnection(@RequestParam String url) {
        try {
            SecurityUtil.startSSRFHook();
            return HttpUtils.HTTPURLConnection(url);
        } catch (SSRFException | IOException e) {
            return e.getMessage();
        } finally {
            SecurityUtil.stopSSRFHook();
        }
    }


    /**
     * The default setting of followRedirects is true.
     * UserAgent is <code>Apache-HttpClient/4.5.12 (Java/1.8.0_102)</code>.
     *
     * http://localhost:8080/ssrf/request/sec?url=http://test.joychou.org
     */
    @GetMapping("/request/sec")
    public String request(@RequestParam String url) {
        try {
            SecurityUtil.startSSRFHook();
            return HttpUtils.request(url);
        } catch (SSRFException | IOException e) {
            return e.getMessage();
        } finally {
            SecurityUtil.stopSSRFHook();
        }
    }


    /**
     * Download the url file.
     * http://localhost:8080/ssrf/openStream?url=file:///etc/passwd
     * <p>
     * new URL(String url).openConnection()
     * new URL(String url).openStream()
     * new URL(String url).getContent()
     */
    @GetMapping("/openStream")
    public void openStream(@RequestParam String url, HttpServletResponse response) throws IOException {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            String downLoadImgFileName = WebUtils.getNameWithoutExtension(url) + "." + WebUtils.getFileExtension(url);
            // download
            response.setHeader("content-disposition", "attachment;fileName=" + downLoadImgFileName);

            URL u = new URL(url);
            int length;
            byte[] bytes = new byte[1024];
            inputStream = u.openStream(); // send request
            outputStream = response.getOutputStream();
            while ((length = inputStream.read(bytes)) > 0) {
                outputStream.write(bytes, 0, length);
            }

        } catch (Exception e) {
            logger.error(e.toString());
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }


    /**
     * The default setting of followRedirects is true.
     * UserAgent is Java/1.8.0_102.
     */
    @GetMapping("/ImageIO/sec")
    public String ImageIO(@RequestParam String url) {
        try {
            SecurityUtil.startSSRFHook();
            HttpUtils.imageIO(url);
        } catch (SSRFException | IOException e) {
            return e.getMessage();
        } finally {
            SecurityUtil.stopSSRFHook();
        }

        return "ImageIO ssrf test";
    }


    @GetMapping("/okhttp/sec")
    public String okhttp(@RequestParam String url) {

        try {
            SecurityUtil.startSSRFHook();
            return HttpUtils.okhttp(url);
        } catch (SSRFException | IOException e) {
            return e.getMessage();
        } finally {
            SecurityUtil.stopSSRFHook();
        }

    }


    /**
     * The default setting of followRedirects is true.
     * UserAgent is <code>Apache-HttpClient/4.5.12 (Java/1.8.0_102)</code>.
     *
     * http://localhost:8080/ssrf/httpclient/sec?url=http://www.baidu.com
     */
    @GetMapping("/httpclient/sec")
    public String HttpClient(@RequestParam String url) {

        try {
            SecurityUtil.startSSRFHook();
            return HttpUtils.httpClient(url);
        } catch (SSRFException | IOException e) {
            return e.getMessage();
        } finally {
            SecurityUtil.stopSSRFHook();
        }

    }


    /**
     * The default setting of followRedirects is true.
     * UserAgent is <code>Jakarta Commons-HttpClient/3.1</code>.
     *
     * http://localhost:8080/ssrf/commonsHttpClient/sec?url=http://www.baidu.com
     */
    @GetMapping("/commonsHttpClient/sec")
    public String commonsHttpClient(@RequestParam String url) {

        try {
            SecurityUtil.startSSRFHook();
            return HttpUtils.commonHttpClient(url);
        } catch (SSRFException | IOException e) {
            return e.getMessage();
        } finally {
            SecurityUtil.stopSSRFHook();
        }

    }

    /**
     * The default setting of followRedirects is true.
     * UserAgent is the useragent of browser.
     *
     * http://localhost:8080/ssrf/Jsoup?url=http://www.baidu.com
     */
    @GetMapping("/Jsoup/sec")
    public String Jsoup(@RequestParam String url) {

        try {
            SecurityUtil.startSSRFHook();
            return HttpUtils.Jsoup(url);
        } catch (SSRFException | IOException e) {
            return e.getMessage();
        } finally {
            SecurityUtil.stopSSRFHook();
        }

    }


    /**
     * The default setting of followRedirects is true.
     * UserAgent is <code>Java/1.8.0_102</code>.
     *
     * http://localhost:8080/ssrf/IOUtils/sec?url=http://www.baidu.com
     */
    @GetMapping("/IOUtils/sec")
    public String IOUtils(String url) {
        try {
            SecurityUtil.startSSRFHook();
            HttpUtils.IOUtils(url);
        } catch (SSRFException | IOException e) {
            return e.getMessage();
        } finally {
            SecurityUtil.stopSSRFHook();
        }

        return "IOUtils ssrf test";
    }


    /**
     * The default setting of followRedirects is true.
     * UserAgent is <code>Apache-HttpAsyncClient/4.1.4 (Java/1.8.0_102)</code>.
     */
    @GetMapping("/HttpSyncClients/vuln")
    public String HttpSyncClients(@RequestParam("url") String url) {
        return HttpUtils.HttpAsyncClients(url);
    }


}

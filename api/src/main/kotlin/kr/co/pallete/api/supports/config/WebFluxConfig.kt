package kr.co.pallete.api.supports.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.buffer.DataBufferUtils
import org.springframework.http.CacheControl
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.server.PathContainer
import org.springframework.web.reactive.config.EnableWebFlux
import org.springframework.web.reactive.config.ResourceHandlerRegistry
import org.springframework.web.reactive.config.WebFluxConfigurer
import org.springframework.web.server.WebFilter
import java.util.concurrent.TimeUnit

@Configuration
@EnableWebFlux
class WebFluxConfig(
    @Value("\${rest-api.path-prefixes}")
    val apiPathPrefixes: Set<String>,
) : WebFluxConfigurer {
    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/**")
            .addResourceLocations("classpath:/static/")
            .setCacheControl(CacheControl.maxAge(7, TimeUnit.DAYS))
    }

    /**
     * SPA 특성상 모든 path 에서 index.html 이 응답 되고 실제 라우팅은 SPA 에서 수행 되어야 합니다.
     * 지정된 API Path 와 확장자가 있는(.css, .jpg등) 리소스가 아닌 경우 index.html 을 응답 합니다.
     *
     * 참고. 다른 해결책
     * 1. Servlet 환경에서는 viewController 를 추가하고, ErrorPage 를 정의하여 유사한 결과를
     * 만들 수 있습니다.
     *
     * 2. Backend 앞에 nginx 를 도입하는 경우 try_files 과 같은 설정으로 404일 때만 index.html
     * 이 응답 되도록 설정 가능합니다.
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    fun singlePageApplicationFilter(): WebFilter = WebFilter { exchange, chain ->
        val request = exchange.request
        val pathElements = request.path.pathWithinApplication().elements().filter { it.value() != "/" }
        if (request.method != HttpMethod.GET) {
            return@WebFilter chain.filter(exchange)
        }

        // If it is not specified as an API Prefix or has no extension, it will go to index.html
        if (pathElements.isEmpty() || (pathElements.first().isAPI() && pathElements.last().hasExtension().not())) {
            val resource = ClassPathResource("/static/index.html")
            if (resource.exists()) {
                val response = exchange.response
                response.statusCode = HttpStatus.OK
                response.headers.contentType = MediaType.TEXT_HTML
                response.headers.setCacheControl(CacheControl.noCache())
                return@WebFilter response.writeWith(
                    DataBufferUtils.read(resource, response.bufferFactory(), 4096),
                )
            }
        }

        chain.filter(exchange)
    }

    private fun PathContainer.Element.isAPI(): Boolean =
        apiPathPrefixes.none { this.value().equals(it, ignoreCase = true) }

    private fun PathContainer.Element.hasExtension(): Boolean =
        value().contains(".")
}

package com.custom.mall.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.custom.mall.domain.FlashSale;
import com.custom.mall.repository.FlashSaleRepository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link FlashSale} entity.
 */
public interface FlashSaleSearchRepository extends ElasticsearchRepository<FlashSale, Long>, FlashSaleSearchRepositoryInternal {}

interface FlashSaleSearchRepositoryInternal {
    Page<FlashSale> search(String query, Pageable pageable);

    Page<FlashSale> search(Query query);

    @Async
    void index(FlashSale entity);

    @Async
    void deleteFromIndexById(Long id);
}

class FlashSaleSearchRepositoryInternalImpl implements FlashSaleSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final FlashSaleRepository repository;

    FlashSaleSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, FlashSaleRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<FlashSale> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<FlashSale> search(Query query) {
        SearchHits<FlashSale> searchHits = elasticsearchTemplate.search(query, FlashSale.class);
        List<FlashSale> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(FlashSale entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), FlashSale.class);
    }
}

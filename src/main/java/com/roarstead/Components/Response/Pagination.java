package com.roarstead.Components.Response;

import com.roarstead.App;
import com.roarstead.Components.Database.Database;
import com.roarstead.Components.Exceptions.NotFoundException;
import com.roarstead.Models.User;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Map;

public class Pagination <T> {
    public static final String LIMIT_KEY = "limit";
    public static final int DEFAULT_LIMIT = 10;
    public static final String OFFSET_KEY = "offset";

    private String modelName;

    private int limit;
    private int offset;

    private Query<T> query;
    private Query countQuery;

    public Pagination(Map<String, String> params) {
        this.limit = params.containsKey(LIMIT_KEY) ? Integer.parseInt(params.get(LIMIT_KEY)) : DEFAULT_LIMIT;

        if(limit > DEFAULT_LIMIT)
            limit = DEFAULT_LIMIT;

        this.offset = params.containsKey(OFFSET_KEY) ? Integer.parseInt(params.get(OFFSET_KEY)) : 0;
    }

    public void setQuery(String strQuery, Map<String, Object> queryParams){
        this.query = createQuery(strQuery, queryParams);
    }

    public void setCountQuery(String strQuery, Map<String, Object> queryParams){
        this.countQuery = createQuery(strQuery, queryParams);
    }

    public List<T> getList() throws NotFoundException {
        long countResults = (long) countQuery.uniqueResult();
        int lastPageNumber = (int) (countResults / limit);

        if(offset > lastPageNumber)
            throw new NotFoundException();

        query.setMaxResults(limit);
        query.setFirstResult(offset * limit);

        return query.list();
    }

    private Query createQuery(String strQuery, Map<String, Object> queryParams){
        Database db = App.getCurrentApp().getDb();
        Query query1 = db.getSession().createQuery(strQuery);

        for (String key :
                queryParams.keySet()) {
            query1.setParameter(key, queryParams.get(key));
        }
        return query1;
    }
}

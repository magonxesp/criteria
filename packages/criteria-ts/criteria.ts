import { useState } from "react";

interface Pagination {
    page: number
    size?: number
}

type Order = "ASC" | "DESC"

interface OrderBy {
    field: string
    order: Order
}

type FilterOperator = "=" // equals
    | "!="  // not equals
    | "<"  // less than
    | ">"  // greater than
    | "<="  // less than or equal
    | ">="  // greater than or equal
    | "~=" // contains
    | "!~=" // not contains
    | "*=" // regex

type FilterValue = string | number | boolean

interface Filter {
    field: string
    value: FilterValue
    operator: FilterOperator
}

export interface Criteria {
    filters: Filter[]
    orderBy: OrderBy[]
    pagination: Pagination
}

export function encodeCriteriaToBase64(criteria: Criteria): string {
    return btoa(JSON.stringify(criteria))
}

const defaultCriteria: Criteria = {
    filters: [],
    orderBy: [],
    pagination: {
        page: 1, size: 10
    }
}

type NamedFilter = {
    [name: string]: Filter
}

// TODO: replace default criteria with default named filters
// TODO: add default "orderby" and pagination
export function useCriteria(initialCriteria: Criteria = defaultCriteria) {
    const [criteria, setCriteria] = useState(initialCriteria);
    const [filters, setFilters] = useState<NamedFilter>({});

    const nextPage = (currentTotalPages: number) => {
        if (currentTotalPages == 0) {
            return
        }

        if (criteria.pagination.page < currentTotalPages) {
            const pagination = { ...criteria.pagination, page: criteria.pagination.page + 1 }
            setCriteria({...criteria, pagination })
        }
    }

    const previousPage = (currentTotalPages: number) => {
        if (currentTotalPages == 0) {
            return
        }

        if (criteria.pagination.page > 1) {
            const pagination = { ...criteria.pagination, page: criteria.pagination.page - 1 }
            setCriteria({...criteria, pagination })
        }
    }

    const setPage = (page: number) => {
        const pagination = { ...criteria.pagination, page }
        setCriteria({...criteria, pagination })
    }

    const setPageSize = (size: number) => {
        const pagination = { ...criteria.pagination, size }
        setCriteria({...criteria, pagination })
    }

    const setFilter = (name: string, field: string, value: FilterValue, operator: FilterOperator = "=") => {
        const filter = { field, value, operator }
        const newFilters = { ...filters, ...{ [name]: filter } }
        setFilters(newFilters)
        setCriteria({...criteria, filters: Object.values(newFilters) })
    }

    const removeFilter = (name: string) => {
        delete filters[name]
        setFilters(filters)
        setCriteria({...criteria, filters: Object.values(filters) })
    }

    const setOrderBy = (field: string, order: Order) => {
        const orderBy = criteria.orderBy.filter(orderBy => orderBy.field !== field)
        orderBy.push({ field, order })

        setCriteria({...criteria, orderBy })
    }

    return {
        criteria,
        nextPage,
        previousPage,
        setPage,
        setPageSize,
        setFilter,
        removeFilter,
        setOrderBy
    }
}
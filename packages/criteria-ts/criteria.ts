import { useState } from "react";
import { useDebounce } from "@/helpers/debounce.ts";

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

type NamedFilters = {
    [name: string]: Filter
}

type NamedOrderBys = {
    [name: string]: OrderBy
}

type UseCriteriaOptions = {
    initialFilters?: NamedFilters,
    initialOrderBy?: NamedOrderBys,
    pagination?: Pagination,
    debounceMs?: number
}

export function encodeCriteriaToBase64(criteria: Criteria): string {
    return btoa(JSON.stringify(criteria))
}

export function useCriteria({ initialFilters, initialOrderBy, pagination, debounceMs }: UseCriteriaOptions) {
    const initialCriteria: Criteria = {
        filters: Object.entries(initialFilters ?? {}).map(([_, filter]) => filter),
        orderBy: Object.entries(initialOrderBy ?? {}).map(([_, orderBy]) => orderBy),
        pagination: pagination ?? { page: 1, size: 10 }
    }

    const [criteria, setCriteria] = useDebounce(initialCriteria, debounceMs ?? 0)
    const [filters, setFilters] = useState<NamedFilters>(initialFilters ?? {})
    const [orderBys, setOrderBys] = useState<NamedOrderBys>(initialOrderBy ?? {})

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

    const setOrderBy = (name: string, field: string, order: Order) => {
        const orderBy = { name, field, order }
        const newOrderBy = { ...orderBys, ...{ [name]: orderBy } }
        setOrderBys(newOrderBy)
        setCriteria({...criteria, orderBy: Object.values(newOrderBy) })
    }

    const removeOrderBy = (name: string) => {
        delete orderBys[name]
        setOrderBys(orderBys)
        setCriteria({...criteria, orderBy: Object.values(orderBys) })
    }

    return {
        criteria,
        nextPage,
        previousPage,
        setPage,
        setPageSize,
        setFilter,
        removeFilter,
        setOrderBy,
        removeOrderBy
    }
}

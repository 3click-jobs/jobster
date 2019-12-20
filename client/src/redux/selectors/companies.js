import { getLocalSelectors, getGlobalSelectors } from './helpers'

export const localCompaniesSelectors = getLocalSelectors('companyId')
export const companiesSelectors = getGlobalSelectors(localCompaniesSelectors, 'entities.companies')
import { getLocalSelectors, getGlobalSelectors } from './helpers'

export const localAccountsSelectors = getLocalSelectors('accountId')
export const accountsSelectors = getGlobalSelectors(localAccountsSelectors, 'entities.accounts')
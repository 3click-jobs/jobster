import { getLocalSelectors, getGlobalSelectors } from './helpers'

export const localOffersSelectors = getLocalSelectors('offerId')
export const offersSelectors = getGlobalSelectors(localOffersSelectors, 'entities.offers')
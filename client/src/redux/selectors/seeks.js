import { getLocalSelectors, getGlobalSelectors } from './helpers'

export const localSeeksSelectors = getLocalSelectors('seekId')
export const seeksSelectors = getGlobalSelectors(localSeeksSelectors, 'entities.seeks')
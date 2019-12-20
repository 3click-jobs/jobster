import { getLocalSelectors, getGlobalSelectors } from './helpers'

export const localPersonsSelectors = getLocalSelectors('personId')
export const personsSelectors = getGlobalSelectors(localPersonsSelectors, 'entities.persons')
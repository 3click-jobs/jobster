import { getLocalSelectors, getGlobalSelectors } from './helpers'

export const localJobTypesSelectors = getLocalSelectors('jobTypeId')
export const jobTypesSelectors = getGlobalSelectors(localJobTypesSelectors, 'entities.jobTypes')
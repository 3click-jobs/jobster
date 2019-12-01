import { schema } from 'normalizr'

const schemasForEntity = (entity, id = 'id') => {
  return {
    [entity]: {
      one: new schema.Entity(entity, {}, { idAttribute: id }),
      many: new schema.Array(entity, {}, { idAttribute: id })
    }
  }
}

export default schemasForEntity
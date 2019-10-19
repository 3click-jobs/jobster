// this shoud be a component, duh!

const getDefaults = (dto) => {

  const init = {}

  for (let prop in dto) {
    init[prop] = dto[prop].default ? dto[prop].default : ''
  }

  return init
}

export default getDefaults
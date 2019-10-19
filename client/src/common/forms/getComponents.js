import React from 'react'

const getComponents = (shape, parent) => {
  const { name, type, regexp, required, options, min, max, placeholder } = shape

  console.log('shape', shape)

  const inputComponent = () => {
    console.log('rendered', name)

    return (
      <input 
        type={type}
        name={name}
        regexp={regexp}
        required={required}
        min={min}
        max={max}
        className="form-control"
        placeholder={placeholder}
        value={parent.state[name]}
        onChange={parent.onChangeHandler}
      />
    )
  }

  // what about onChange() ?

  return inputComponent()
}

export default getComponents
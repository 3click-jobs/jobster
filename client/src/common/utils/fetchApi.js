import { getAuthHeader } from "../auth";

const getOne = async (url, id) => {
  try {
    const resp = await fetch(`${url}/${id}`, {
      headers: {
        ...getAuthHeader()
      }
    })

    if (resp.ok) {
      const json = await resp.json()
      return json
    }

    else {
      throw new Error('contacted api but received other than ok...')
    }
  } catch (err) {
    throw err
  }
}

const getAll = async (url) => {
  try {
    const resp = await fetch(`${url}`, {
      headers: {
        ...getAuthHeader()
      }
    })

    if (resp.ok) {
      const json = await resp.json()
      return json
    }

    else {
      throw new Error('contacted api but received other than ok...')
    }
  } catch (err) {
    throw err
  }
}

const getAny = async (url, query) => {
  try {
    const resp = await fetch(`${url}/query?${query}`, {
      headers: {
        ...getAuthHeader()
      }
    })

    if (resp.ok) {
      const json = await resp.json()
      return json
    }

    else {
      throw new Error('contacted api but received other than ok...')
    }
  } catch (err) {
    throw err
  }
}

const createResource = async (url, payload) => {
  try {
    const resp = await fetch(`${url}`, {
      method: 'POST',
      headers: {
        ...getAuthHeader(),
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(payload)
    })

    if (resp.ok) {
      const json = await resp.json()
      return json
    }

    else {
      throw new Error('contacted api but received other than ok...')
    }
  } catch (err) {
    throw err
  }
}

const updateResource = async (url, id, payload) => {
  console.log('update resource payload: ', payload)
  try {
    const resp = await fetch(`${url}/${id}`, {
      method: 'PUT',
      headers: {
        ...getAuthHeader(),
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(payload)
    })

    if (resp.ok) {
      const json = await resp.json()
      return json
    }

    else {
      throw new Error('contacted api but received other than ok...')
    }
  } catch (err) {
    throw err
  }
}

const updatePassword = async (url, id, payload) => {
  console.log('update password payload: ', payload)
  try {
    const resp = await fetch(`${url}/${id}/changePassword`, {
      method: 'PUT',
      headers: {
        ...getAuthHeader(),
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(payload)
    })

    if (resp.ok) {
      const json = await resp.json()
      return json
    }

    else {
      throw new Error('contacted api but received other than ok...')
    }
  } catch (err) {
    throw err
  }
}

const deleteResource = async (url, id) => {
  try {
    const resp = await fetch(`${url}/${id}`, {
      method: 'DELETE',
      headers: {
        ...getAuthHeader()
      }
    })

    if (resp.ok) {
      const json = await resp.json()
      return json
    }

    else {
      throw new Error('contacted api but received other than ok...')
    }
  } catch (err) {
    throw err
  }
}

const decline = async (url, payload) => {
  try {
    const resp = await fetch(`${url}`, {
      method: 'POST',
      headers: {
        ...getAuthHeader(),
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(payload)
    })

    if (resp.ok) {
      const json = await resp.json()
      return json
    }

    else {
      throw new Error('contacted api but received other than ok...')
    }
  } catch (err) {
    throw err
  }
}

const accept = async (url, payload) => {
  try {
    const resp = await fetch(`${url}`, {
      method: 'POST',
      headers: {
        ...getAuthHeader(),
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(payload)
    })

    if (resp.ok) {
      const json = await resp.json()
      return json
    }

    else {
      throw new Error('contacted api but received other than ok...')
    }
  } catch (err) {
    throw err
  }
}


export { getOne, getAll, getAny, createResource, updateResource, deleteResource, updatePassword, decline, accept }
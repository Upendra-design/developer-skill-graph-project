import axios from 'axios'

// Base URL is configurable via VITE_API_URL so the same build can talk to
// a local backend during development and a deployed backend in production.
const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api'

const client = axios.create({
  baseURL: API_URL,
  timeout: 10000,
})

export const api = {
  getDashboard: () => client.get('/dashboard'),
  getHealth: () => client.get('/health'),

  getDevelopers: (search = '') => client.get('/developers', { params: search ? { search } : {} }),
  getDeveloperDetails: (id) => client.get(`/developers/${id}`),
  getDeveloperConnectedTechnologies: (id) => client.get(`/developers/${id}/connected-technologies`),
  getRelatedDevelopers: (id) => client.get(`/developers/${id}/related-developers`),

  getProjects: () => client.get('/projects'),
  getProjectDetails: (id) => client.get(`/projects/${id}`),
  getCandidateDevelopers: (id) => client.get(`/projects/${id}/candidate-developers`),

  getSkills: () => client.get('/skills'),
  getTechnologies: () => client.get('/technologies'),
}

export default client

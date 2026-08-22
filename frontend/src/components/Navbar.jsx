import { NavLink } from 'react-router-dom'

export default function Navbar() {
  return (
    <header className="navbar">
      <div className="navbar-inner">
        <div className="navbar-brand">
          <span className="brand-dot" />
          Dev Skill Graph
        </div>
        <nav className="navbar-links">
          <NavLink to="/" end className={({ isActive }) => isActive ? 'active' : ''}>Dashboard</NavLink>
          <NavLink to="/developers" className={({ isActive }) => isActive ? 'active' : ''}>Developers</NavLink>
          <NavLink to="/projects" className={({ isActive }) => isActive ? 'active' : ''}>Projects</NavLink>
          <NavLink to="/explorer" className={({ isActive }) => isActive ? 'active' : ''}>Graph Explorer</NavLink>
        </nav>
      </div>
    </header>
  )
}

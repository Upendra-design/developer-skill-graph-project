export default function Tag({ children, tone = 'default' }) {
  return <span className={`tag tone-${tone}`}>{children}</span>
}

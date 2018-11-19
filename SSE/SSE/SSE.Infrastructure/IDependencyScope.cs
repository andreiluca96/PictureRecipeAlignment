using System;

namespace SSE.Infrastructure
{
    public interface IDependencyScope
    {
        T Resolve<T>();
        object Resolve(Type serviceType);
    }
}
using Autofac;
using System;

namespace SSE.Infrastructure
{
    public class AutofacDependencyScope : IDependencyScope
    {
        private readonly ILifetimeScope scope;

        public AutofacDependencyScope(ILifetimeScope scope)
        {
            this.scope = scope;
        }

        public T Resolve<T>()
        {
            return scope.Resolve<T>();
        }

        public object Resolve(Type serviceType)
        {
            return scope.Resolve(serviceType);
        }
    }
}

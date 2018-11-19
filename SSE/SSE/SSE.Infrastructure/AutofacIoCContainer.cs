using Autofac;
using SSE.Infrastructure.Modules;
using System;
using System.Collections.Generic;
using System.Text;

namespace SSE.Infrastructure
{
    public class AutofacIoCContainer : Module
    {
        protected override void Load(ContainerBuilder builder)
        {
            builder.RegisterModule<BusinessInjectionModule>();
            builder.RegisterModule<CommonInjectionModule>();
        }
    }
}
